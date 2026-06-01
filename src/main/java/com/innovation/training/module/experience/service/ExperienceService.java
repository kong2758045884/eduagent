package com.innovation.training.module.experience.service;

import com.innovation.training.common.BusinessException;
import com.innovation.training.common.ErrorCode;
import com.innovation.training.module.experience.dto.ExperienceBookResponse;
import com.innovation.training.module.experience.dto.ExperienceItemResponse;
import com.innovation.training.module.experience.dto.ShareExperienceRequest;
import com.innovation.training.module.lesson.dto.LessonResponse;
import com.innovation.training.module.lesson.dto.ReflectionResponse;
import com.innovation.training.module.lesson.service.LessonService;
import com.innovation.training.module.report.dto.ReportRequest;
import com.innovation.training.module.report.dto.ReportResponse;
import com.innovation.training.module.report.service.ReportService;
import com.innovation.training.module.resource.dto.CreateResourceRequest;
import com.innovation.training.module.resource.dto.ResourceResponse;
import com.innovation.training.module.resource.service.ResourceService;
import com.innovation.training.module.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExperienceService {

    private final LessonService lessonService;
    private final ResourceService resourceService;
    private final ReportService reportService;

    public ExperienceService(LessonService lessonService,
                             ResourceService resourceService,
                             ReportService reportService) {
        this.lessonService = lessonService;
        this.resourceService = resourceService;
        this.reportService = reportService;
    }

    public ExperienceBookResponse book(Long userId) {
        List<LessonResponse> lessons = lessonService.listLessons(userId);
        List<ReflectionResponse> reflections = lessonService.listReflections(userId, null);
        Map<Long, List<ReflectionResponse>> byLesson = reflections.stream()
                .collect(Collectors.groupingBy(ReflectionResponse::getLessonId));
        List<ExperienceItemResponse> items = lessons.stream()
                .map(lesson -> new ExperienceItemResponse(lesson, byLesson.getOrDefault(lesson.getId(), List.of())))
                .toList();
        return new ExperienceBookResponse((long) lessons.size(), (long) reflections.size(), items);
    }

    public ResourceResponse share(User user, ShareExperienceRequest request) {
        if (Boolean.FALSE.equals(request.getAuthorized())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "分享前需要确认授权");
        }
        LessonResponse lesson = null;
        ReflectionResponse reflection = null;
        if (request.getLessonId() != null) {
            lesson = lessonService.listLessons(user.getId()).stream()
                    .filter(item -> item.getId().equals(request.getLessonId()))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST, "教案不存在"));
        }
        if (request.getReflectionId() != null) {
            reflection = lessonService.listReflections(user.getId(), request.getLessonId()).stream()
                    .filter(item -> item.getId().equals(request.getReflectionId()))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST, "反思不存在"));
        }

        String content = buildContent(lesson, reflection);
        CreateResourceRequest resource = new CreateResourceRequest();
        resource.setTitle(request.getTitle().trim());
        resource.setSummary(defaultText(request.getSummary(), lesson == null ? "教学经验沉淀" : lesson.getSummary()));
        resource.setContent(content);
        resource.setResourceType(defaultText(request.getCategory(), "experience"));
        resource.setSubject(defaultText(user.getSubject(), "数学"));
        resource.setGrade(user.getGrade());
        resource.setCounty(user.getCounty());
        resource.setSchool(user.getSchool());
        resource.setSourceType(reflection != null ? "reflection" : "lesson");
        resource.setSourceId(reflection != null ? reflection.getId() : lesson == null ? null : lesson.getId());
        return resourceService.create(user.getId(), resource);
    }

    public ReportResponse export(Long userId, String title) {
        ReportRequest request = new ReportRequest();
        request.setTitle(StringUtils.hasText(title) ? title.trim() : "我的教学经验册");
        return reportService.experienceBook(userId, request);
    }

    private String buildContent(LessonResponse lesson, ReflectionResponse reflection) {
        StringBuilder builder = new StringBuilder();
        if (lesson != null) {
            builder.append("# ").append(lesson.getTitle()).append("\n\n");
            builder.append(defaultText(lesson.getContent(), lesson.getSummary())).append("\n\n");
        }
        if (reflection != null) {
            builder.append("## 课后心得\n");
            builder.append(reflection.getText()).append("\n\n");
            builder.append("## AI 摘要\n").append(defaultText(reflection.getAiSummary(), "暂无摘要")).append("\n");
        }
        if (builder.isEmpty()) {
            builder.append("教学经验沉淀");
        }
        return builder.toString();
    }

    private String defaultText(String value, String fallback) {
        return StringUtils.hasText(value) ? value.trim() : fallback;
    }
}
