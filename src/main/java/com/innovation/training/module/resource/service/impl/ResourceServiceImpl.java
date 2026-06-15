package com.innovation.training.module.resource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.innovation.training.common.BusinessException;
import com.innovation.training.common.ErrorCode;
import com.innovation.training.module.resource.comment.ResourceComment;
import com.innovation.training.module.resource.dto.CommentResponse;
import com.innovation.training.module.resource.dto.CreateCommentRequest;
import com.innovation.training.module.resource.dto.CreateResourceRequest;
import com.innovation.training.module.resource.dto.ResourceResponse;
import com.innovation.training.module.resource.entity.TeachingResource;
import com.innovation.training.module.resource.mapper.ResourceCommentMapper;
import com.innovation.training.module.resource.mapper.TeachingResourceMapper;
import com.innovation.training.module.resource.service.ResourceService;
import com.innovation.training.module.growth.service.GrowthService;
import com.innovation.training.module.notification.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {

    private static final String AUDIT_APPROVED = "approved";

    private final TeachingResourceMapper teachingResourceMapper;
    private final ResourceCommentMapper resourceCommentMapper;
    private final GrowthService growthService;
    private final NotificationService notificationService;

    public ResourceServiceImpl(TeachingResourceMapper teachingResourceMapper,
                               ResourceCommentMapper resourceCommentMapper,
                               GrowthService growthService,
                               NotificationService notificationService) {
        this.teachingResourceMapper = teachingResourceMapper;
        this.resourceCommentMapper = resourceCommentMapper;
        this.growthService = growthService;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResourceResponse create(Long userId, CreateResourceRequest request) {
        TeachingResource resource = new TeachingResource();
        resource.setUserId(userId);
        resource.setTitle(request.getTitle().trim());
        resource.setSummary(trimToNull(request.getSummary()));
        resource.setContent(trimToNull(request.getContent()));
        resource.setResourceType(defaultText(request.getResourceType(), "lesson"));
        resource.setSubject(defaultText(request.getSubject(), "数学"));
        resource.setGrade(trimToNull(request.getGrade()));
        resource.setCounty(trimToNull(request.getCounty()));
        resource.setSchool(trimToNull(request.getSchool()));
        resource.setCoverUrl(trimToNull(request.getCoverUrl()));
        resource.setMediaUrl(trimToNull(request.getMediaUrl()));
        resource.setDuration(trimToNull(request.getDuration()));
        resource.setUploader(trimToNull(request.getUploader()));
        resource.setTags(trimToNull(request.getTags()));
        resource.setSourceType(trimToNull(request.getSourceType()));
        resource.setSourceId(request.getSourceId());
        resource.setAuditStatus("approved");
        resource.setLikes(0);
        resource.setViewCount(0);
        resource.setFavoriteCount(0);
        LocalDateTime now = LocalDateTime.now();
        resource.setCreatedAt(now);
        resource.setUpdatedAt(now);
        teachingResourceMapper.insert(resource);
        growthService.record(userId, "resource_shared", "分享教研资源：" + resource.getTitle(),
                resource.getSummary(), "resource", resource.getId());
        notificationService.notify(userId, "资源已提交审核", "《" + resource.getTitle() + "》已进入县域资源审核队列。",
                "audit", "resource", resource.getId());
        return ResourceResponse.from(resource);
    }

    @Override
    public List<ResourceResponse> list(String county, String resourceType, String subject, String grade) {
        LambdaQueryWrapper<TeachingResource> wrapper = new LambdaQueryWrapper<TeachingResource>()
                .eq(TeachingResource::getAuditStatus, AUDIT_APPROVED)
                .orderByDesc(TeachingResource::getLikes)
                .orderByDesc(TeachingResource::getViewCount)
                .orderByDesc(TeachingResource::getCreatedAt);
        if (StringUtils.hasText(county)) {
            wrapper.eq(TeachingResource::getCounty, county);
        }
        if (StringUtils.hasText(resourceType)) {
            wrapper.eq(TeachingResource::getResourceType, resourceType);
        }
        if (StringUtils.hasText(subject)) {
            wrapper.eq(TeachingResource::getSubject, subject);
        }
        if (StringUtils.hasText(grade)) {
            wrapper.eq(TeachingResource::getGrade, grade);
        }
        return teachingResourceMapper.selectList(wrapper)
                .stream()
                .map(this::toResponseWithCommentCount)
                .toList();
    }

    @Override
    public List<ResourceResponse> listPendingAudit() {
        return teachingResourceMapper.selectList(new LambdaQueryWrapper<TeachingResource>()
                        .eq(TeachingResource::getAuditStatus, "pending")
                        .orderByDesc(TeachingResource::getCreatedAt))
                .stream()
                .map(this::toResponseWithCommentCount)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResourceResponse like(Long resourceId) {
        TeachingResource resource = requireResource(resourceId);
        resource.setLikes((resource.getLikes() == null ? 0 : resource.getLikes()) + 1);
        resource.setUpdatedAt(LocalDateTime.now());
        teachingResourceMapper.updateById(resource);
        return toResponseWithCommentCount(resource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResourceResponse favorite(Long userId, Long resourceId) {
        TeachingResource resource = requireResource(resourceId);
        resource.setFavoriteCount((resource.getFavoriteCount() == null ? 0 : resource.getFavoriteCount()) + 1);
        resource.setUpdatedAt(LocalDateTime.now());
        teachingResourceMapper.updateById(resource);
        growthService.record(userId, "resource_favorited", "收藏资源：" + resource.getTitle(),
                resource.getSummary(), "resource", resourceId);
        return toResponseWithCommentCount(resource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResourceResponse watched(Long userId, Long resourceId) {
        TeachingResource resource = requireResource(resourceId);
        resource.setViewCount((resource.getViewCount() == null ? 0 : resource.getViewCount()) + 1);
        resource.setUpdatedAt(LocalDateTime.now());
        teachingResourceMapper.updateById(resource);
        growthService.record(userId, "resource_watched", "观看资源：" + resource.getTitle(),
                resource.getSummary(), "resource", resourceId);
        return toResponseWithCommentCount(resource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResourceResponse review(Long resourceId, String auditStatus) {
        TeachingResource resource = requireResource(resourceId);
        resource.setAuditStatus(StringUtils.hasText(auditStatus) ? auditStatus.trim() : AUDIT_APPROVED);
        resource.setUpdatedAt(LocalDateTime.now());
        teachingResourceMapper.updateById(resource);
        notificationService.notify(resource.getUserId(), "资源审核结果",
                "《" + resource.getTitle() + "》审核状态：" + resource.getAuditStatus(),
                "audit", "resource", resource.getId());
        return toResponseWithCommentCount(resource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentResponse comment(Long userId, Long resourceId, CreateCommentRequest request) {
        requireResource(resourceId);
        ResourceComment comment = new ResourceComment();
        comment.setResourceId(resourceId);
        comment.setUserId(userId);
        comment.setContent(request.getContent().trim());
        comment.setCreatedAt(LocalDateTime.now());
        resourceCommentMapper.insert(comment);
        growthService.record(userId, "resource_commented", "评论教研资源",
                comment.getContent(), "resource", resourceId);
        return CommentResponse.from(comment);
    }

    @Override
    public List<CommentResponse> listComments(Long resourceId) {
        return resourceCommentMapper.selectList(new LambdaQueryWrapper<ResourceComment>()
                        .eq(ResourceComment::getResourceId, resourceId)
                        .orderByDesc(ResourceComment::getCreatedAt))
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    private TeachingResource requireResource(Long resourceId) {
        TeachingResource resource = teachingResourceMapper.selectById(resourceId);
        if (resource == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "资源不存在");
        }
        return resource;
    }

    private ResourceResponse toResponseWithCommentCount(TeachingResource resource) {
        ResourceResponse response = ResourceResponse.from(resource);
        Long count = resourceCommentMapper.selectCount(new LambdaQueryWrapper<ResourceComment>()
                .eq(ResourceComment::getResourceId, resource.getId()));
        response.setCommentCount(count == null ? 0 : count.intValue());
        return response;
    }

    private String trimToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private String defaultText(String value, String fallback) {
        return StringUtils.hasText(value) ? value.trim() : fallback;
    }
}
