package com.innovation.training.module.profile.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.innovation.training.module.diagnosis.entity.DiagnosisReport;
import com.innovation.training.module.diagnosis.mapper.DiagnosisReportMapper;
import com.innovation.training.module.file.entity.AppFile;
import com.innovation.training.module.file.mapper.AppFileMapper;
import com.innovation.training.module.growth.dto.GrowthEventResponse;
import com.innovation.training.module.growth.entity.GrowthEvent;
import com.innovation.training.module.growth.mapper.GrowthEventMapper;
import com.innovation.training.module.lesson.entity.LessonDraft;
import com.innovation.training.module.lesson.entity.LessonReflection;
import com.innovation.training.module.lesson.mapper.LessonDraftMapper;
import com.innovation.training.module.lesson.mapper.LessonReflectionMapper;
import com.innovation.training.module.profile.dto.ProfileDashboardResponse;
import com.innovation.training.module.profile.dto.UpdateProfileRequest;
import com.innovation.training.module.research.entity.ResearchTopic;
import com.innovation.training.module.research.mapper.ResearchTopicMapper;
import com.innovation.training.module.resource.entity.TeachingResource;
import com.innovation.training.module.resource.mapper.TeachingResourceMapper;
import com.innovation.training.module.user.dto.UserResponse;
import com.innovation.training.module.user.entity.User;
import com.innovation.training.module.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProfileService {

    private final UserMapper userMapper;
    private final LessonDraftMapper lessonDraftMapper;
    private final LessonReflectionMapper reflectionMapper;
    private final DiagnosisReportMapper diagnosisReportMapper;
    private final TeachingResourceMapper resourceMapper;
    private final ResearchTopicMapper researchTopicMapper;
    private final GrowthEventMapper growthEventMapper;
    private final AppFileMapper appFileMapper;

    public ProfileService(UserMapper userMapper,
                          LessonDraftMapper lessonDraftMapper,
                          LessonReflectionMapper reflectionMapper,
                          DiagnosisReportMapper diagnosisReportMapper,
                          TeachingResourceMapper resourceMapper,
                          ResearchTopicMapper researchTopicMapper,
                          GrowthEventMapper growthEventMapper,
                          AppFileMapper appFileMapper) {
        this.userMapper = userMapper;
        this.lessonDraftMapper = lessonDraftMapper;
        this.reflectionMapper = reflectionMapper;
        this.diagnosisReportMapper = diagnosisReportMapper;
        this.resourceMapper = resourceMapper;
        this.researchTopicMapper = researchTopicMapper;
        this.growthEventMapper = growthEventMapper;
        this.appFileMapper = appFileMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserResponse update(User user, UpdateProfileRequest request) {
        user.setNickname(defaultText(request.getNickname(), user.getNickname()));
        user.setTeacherType(normalizeTeacherType(defaultText(request.getTeacherType(), user.getTeacherType())));
        user.setCounty(trimToNull(request.getCounty()));
        user.setSchool(trimToNull(request.getSchool()));
        user.setAvatarUrl(trimToNull(request.getAvatarUrl()));
        user.setRealName(trimToNull(request.getRealName()));
        user.setPhone(trimToNull(request.getPhone()));
        user.setSubject(defaultText(request.getSubject(), "数学"));
        user.setGrade(trimToNull(request.getGrade()));
        user.setTitle(trimToNull(request.getTitle()));
        user.setTeachingYears(request.getTeachingYears());
        user.setBio(trimToNull(request.getBio()));
        user.setExpertiseTags(trimToNull(request.getExpertiseTags()));
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
        return UserResponse.from(user);
    }

    public ProfileDashboardResponse dashboard(User user) {
        Long userId = user.getId();
        Map<String, Long> counters = new LinkedHashMap<>();
        counters.put("lessons", countLessons(userId));
        counters.put("reflections", countReflections(userId));
        counters.put("diagnoses", countDiagnoses(userId));
        counters.put("sharedResources", countResources(userId));
        counters.put("researchTopics", countResearchTopics(userId));
        counters.put("growthEvents", countGrowthEvents(userId));
        counters.put("files", countFiles(userId));

        List<GrowthEventResponse> recentEvents = growthEventMapper.selectList(new LambdaQueryWrapper<GrowthEvent>()
                        .eq(GrowthEvent::getUserId, userId)
                        .orderByDesc(GrowthEvent::getEventTime)
                        .last("LIMIT 8"))
                .stream()
                .map(GrowthEventResponse::from)
                .toList();
        return new ProfileDashboardResponse(UserResponse.from(user), counters, recentEvents);
    }

    private Long countLessons(Long userId) {
        return value(lessonDraftMapper.selectCount(new LambdaQueryWrapper<LessonDraft>().eq(LessonDraft::getUserId, userId)));
    }

    private Long countReflections(Long userId) {
        return value(reflectionMapper.selectCount(new LambdaQueryWrapper<LessonReflection>().eq(LessonReflection::getUserId, userId)));
    }

    private Long countDiagnoses(Long userId) {
        return value(diagnosisReportMapper.selectCount(new LambdaQueryWrapper<DiagnosisReport>().eq(DiagnosisReport::getUserId, userId)));
    }

    private Long countResources(Long userId) {
        return value(resourceMapper.selectCount(new LambdaQueryWrapper<TeachingResource>().eq(TeachingResource::getUserId, userId)));
    }

    private Long countResearchTopics(Long userId) {
        return value(researchTopicMapper.selectCount(new LambdaQueryWrapper<ResearchTopic>().eq(ResearchTopic::getUserId, userId)));
    }

    private Long countGrowthEvents(Long userId) {
        return value(growthEventMapper.selectCount(new LambdaQueryWrapper<GrowthEvent>().eq(GrowthEvent::getUserId, userId)));
    }

    private Long countFiles(Long userId) {
        return value(appFileMapper.selectCount(new LambdaQueryWrapper<AppFile>().eq(AppFile::getUserId, userId)));
    }

    private Long value(Long count) {
        return count == null ? 0L : count;
    }

    private String trimToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private String defaultText(String value, String fallback) {
        return StringUtils.hasText(value) ? value.trim() : fallback;
    }

    private String normalizeTeacherType(String teacherType) {
        if (!StringUtils.hasText(teacherType)) {
            return "senior";
        }
        String value = teacherType.trim();
        return ("senior".equals(value) || "mid".equals(value) || "novice".equals(value)) ? value : "senior";
    }
}
