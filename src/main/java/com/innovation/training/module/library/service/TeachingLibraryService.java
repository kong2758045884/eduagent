package com.innovation.training.module.library.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.innovation.training.module.growth.service.GrowthService;
import com.innovation.training.module.resource.dto.ResourceResponse;
import com.innovation.training.module.resource.entity.TeachingResource;
import com.innovation.training.module.resource.mapper.TeachingResourceMapper;
import com.innovation.training.module.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import com.innovation.training.module.library.dto.UploadVideoRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeachingLibraryService {

    private final TeachingResourceMapper resourceMapper;
    private final GrowthService growthService;

    public TeachingLibraryService(TeachingResourceMapper resourceMapper, GrowthService growthService) {
        this.resourceMapper = resourceMapper;
        this.growthService = growthService;
    }

    public List<ResourceResponse> videos(User user, String tag) {
        LambdaQueryWrapper<TeachingResource> wrapper = new LambdaQueryWrapper<TeachingResource>()
                .eq(TeachingResource::getAuditStatus, "approved")
                .in(TeachingResource::getResourceType, List.of("video", "experience"))
                .orderByDesc(TeachingResource::getCreatedAt);
        if (StringUtils.hasText(tag)) {
            wrapper.and(item -> item.like(TeachingResource::getTitle, tag.trim())
                    .or()
                    .like(TeachingResource::getSummary, tag.trim())
                    .or()
                    .like(TeachingResource::getContent, tag.trim()));
        }
        List<ResourceResponse> resources = resourceMapper.selectList(wrapper).stream()
                .map(ResourceResponse::from)
                .toList();
        return resources.isEmpty() ? seedVideos(user) : resources;
    }

    public void favorite(User user, Long resourceId) {
        if (resourceId != null && resourceId > 0) {
            TeachingResource resource = resourceMapper.selectById(resourceId);
            if (resource != null) {
                resource.setFavoriteCount((resource.getFavoriteCount() == null ? 0 : resource.getFavoriteCount()) + 1);
                resource.setUpdatedAt(LocalDateTime.now());
                resourceMapper.updateById(resource);
            }
        }
        growthService.record(user.getId(), "teaching_library_favorite", "收藏本土教法资源",
                "资源 ID：" + resourceId, "resource", resourceId);
    }

    public ResourceResponse uploadVideo(User user, UploadVideoRequest req) {
        TeachingResource r = new TeachingResource();
        r.setUserId(user.getId());
        r.setTitle(req.getTitle() != null ? req.getTitle().trim() : "未命名视频");
        r.setSummary(req.getSummary() != null ? req.getSummary().trim() : "");
        r.setContent(req.getContent() != null ? req.getContent().trim() : "");
        r.setResourceType("video");
        r.setSubject("数学");
        r.setGrade(user.getGrade());
        r.setCounty(user.getCounty());
        r.setSchool(user.getSchool());
        r.setMediaUrl(req.getMediaUrl() != null ? req.getMediaUrl().trim() : "");
        r.setCoverUrl(req.getCoverUrl() != null ? req.getCoverUrl().trim() : "");
        r.setDuration(req.getDuration() != null ? req.getDuration().trim() : "");
        r.setUploader(user.getNickname() != null ? user.getNickname() : user.getUsername());
        r.setTags(req.getCategory() != null ? req.getCategory().trim() : "其他");
        r.setAuditStatus("approved");
        r.setLikes(0); r.setViewCount(0); r.setFavoriteCount(0);
        LocalDateTime now = LocalDateTime.now();
        r.setCreatedAt(now); r.setUpdatedAt(now);
        resourceMapper.insert(r);
        return ResourceResponse.from(r);
    }

    public ResourceResponse updateVideo(User user, Long id, UploadVideoRequest req) {
        TeachingResource r = resourceMapper.selectById(id);
        if (r == null || !r.getUserId().equals(user.getId())) throw new RuntimeException("无权操作");
        if (req.getTitle() != null) r.setTitle(req.getTitle().trim());
        if (req.getSummary() != null) r.setSummary(req.getSummary().trim());
        if (req.getCategory() != null) r.setTags(req.getCategory().trim());
        r.setUpdatedAt(LocalDateTime.now());
        resourceMapper.updateById(r);
        return ResourceResponse.from(r);
    }

    public void deleteVideo(User user, Long id) {
        TeachingResource r = resourceMapper.selectById(id);
        if (r == null || !r.getUserId().equals(user.getId())) throw new RuntimeException("无权操作");
        resourceMapper.deleteById(id);
    }

    public void watched(User user, Long resourceId) {
        if (resourceId != null && resourceId > 0) {
            TeachingResource resource = resourceMapper.selectById(resourceId);
            if (resource != null) {
                resource.setViewCount((resource.getViewCount() == null ? 0 : resource.getViewCount()) + 1);
                resource.setUpdatedAt(LocalDateTime.now());
                resourceMapper.updateById(resource);
            }
        }
        growthService.record(user.getId(), "teaching_library_watched", "观看本土教法资源",
                "资源 ID：" + resourceId, "resource", resourceId);
    }

    private List<ResourceResponse> seedVideos(User user) {
        List<TeachingResource> seeds = new ArrayList<>();
        seeds.add(seed(-1L, "如何用方言解释鸡兔同笼", "方言教学 · 数学模型", "本县老教师演示如何把抽象数量关系转成学生熟悉的话。", user));
        seeds.add(seed(-2L, "在操场教周长与长度估测", "乡土场景 · 操作活动", "利用田埂、操场边线和步测活动建立量感。", user));
        seeds.add(seed(-3L, "低年级课堂表达启动技巧", "课堂管理 · 表达训练", "通过句式模板和两人互说降低表达门槛。", user));
        return seeds.stream().map(ResourceResponse::from).toList();
    }

    private TeachingResource seed(Long id, String title, String summary, String content, User user) {
        TeachingResource resource = new TeachingResource();
        resource.setId(id);
        resource.setUserId(0L);
        resource.setTitle(title);
        resource.setSummary(summary);
        resource.setContent(content);
        resource.setResourceType("video");
        resource.setSubject("数学");
        resource.setGrade(user.getGrade());
        resource.setCounty(user.getCounty());
        resource.setSchool(user.getSchool());
        resource.setCoverUrl("/uploads/mock/covers/local-teaching.png");
        resource.setMediaUrl("/uploads/mock/videos/local-teaching.mp4");
        resource.setDuration("10:00");
        resource.setUploader("平台内置");
        resource.setTags(summary);
        resource.setAuditStatus("approved");
        resource.setLikes(0);
        resource.setViewCount(0);
        resource.setFavoriteCount(0);
        resource.setCreatedAt(LocalDateTime.now());
        return resource;
    }
}
