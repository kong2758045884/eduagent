package com.innovation.training.module.report.service;

import com.innovation.training.module.file.dto.FileResponse;
import com.innovation.training.module.file.service.AppFileService;
import com.innovation.training.module.growth.dto.GrowthEventResponse;
import com.innovation.training.module.growth.service.GrowthService;
import com.innovation.training.module.lesson.dto.LessonResponse;
import com.innovation.training.module.lesson.dto.ReflectionResponse;
import com.innovation.training.module.lesson.service.LessonService;
import com.innovation.training.module.report.dto.ReportRequest;
import com.innovation.training.module.report.dto.ReportResponse;
import com.innovation.training.module.research.dto.ResearchTopicResponse;
import com.innovation.training.module.research.service.ResearchService;
import com.innovation.training.support.LocalFileStorageService;
import com.innovation.training.support.StoredFile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private final LessonService lessonService;
    private final GrowthService growthService;
    private final ResearchService researchService;
    private final ReportDocumentBuilder builder;
    private final LocalFileStorageService fileStorageService;
    private final AppFileService appFileService;

    public ReportService(LessonService lessonService,
                         GrowthService growthService,
                         ResearchService researchService,
                         ReportDocumentBuilder builder,
                         LocalFileStorageService fileStorageService,
                         AppFileService appFileService) {
        this.lessonService = lessonService;
        this.growthService = growthService;
        this.researchService = researchService;
        this.builder = builder;
        this.fileStorageService = fileStorageService;
        this.appFileService = appFileService;
    }

    public ReportResponse experienceBook(Long userId, ReportRequest request) {
        String title = titleOr(request, "我的教学经验册");
        List<String> sections = new ArrayList<>();
        sections.add("生成日期：" + LocalDate.now());
        for (LessonResponse lesson : filterLessons(lessonService.listLessons(userId), request)) {
            sections.add("教案：" + lesson.getTitle() + "\n" + nullToEmpty(lesson.getSummary()));
        }
        for (ReflectionResponse reflection : filterReflections(lessonService.listReflections(userId, null), request)) {
            sections.add("反思：" + reflection.getText() + "\n摘要：" + nullToEmpty(reflection.getAiSummary()));
        }
        byte[] bytes = builder.buildPdf(title, sections);
        return saveGenerated(userId, bytes, title + ".pdf", "application/pdf", "experience_book");
    }

    public ReportResponse assessmentPackage(Long userId, ReportRequest request) {
        String title = titleOr(request, "教师考核材料包");
        List<String> sections = new ArrayList<>();
        List<LessonResponse> lessons = filterLessons(lessonService.listLessons(userId), request);
        List<ReflectionResponse> reflections = filterReflections(lessonService.listReflections(userId, null), request);
        List<ResearchTopicResponse> topics = filterTopics(researchService.list(userId), request);
        if (request.getStartDate() != null || request.getEndDate() != null) {
            sections.add("统计范围：" + nullToEmpty(request.getStartDate() == null ? null : request.getStartDate().toString())
                    + " 至 " + nullToEmpty(request.getEndDate() == null ? null : request.getEndDate().toString()));
        }
        sections.add("备课总数：" + lessons.size());
        sections.add("反思记录数：" + reflections.size());
        sections.add("课题记录数：" + topics.size());
        lessons.forEach(lesson -> sections.add("备课记录：" + lesson.getTitle() + "\n" + nullToEmpty(lesson.getSummary())));
        topics.forEach(topic -> sections.add("课题成果：" + topic.getTitle() + "\n" + nullToEmpty(topic.getMeta())));
        byte[] bytes = builder.buildDocx(title, sections);
        return saveGenerated(userId, bytes, title + ".docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "assessment_package");
    }

    public ReportResponse growthReport(Long userId, ReportRequest request) {
        String title = titleOr(request, "新任教师成长报告");
        List<String> sections = new ArrayList<>();
        List<GrowthEventResponse> events = filterEvents(growthService.list(userId), request);
        sections.add("成长事件总数：" + events.size());
        events.forEach(event -> sections.add(event.getEventTime() + "｜" + event.getTitle() + "\n" + nullToEmpty(event.getContent())));
        byte[] bytes = builder.buildPdf(title, sections);
        return saveGenerated(userId, bytes, title + ".pdf", "application/pdf", "growth_report");
    }

    private ReportResponse saveGenerated(Long userId, byte[] bytes, String fileName, String contentType, String reportType) {
        StoredFile storedFile = fileStorageService.storeBytes(bytes, fileName, contentType, "reports");
        FileResponse file = appFileService.record(userId, storedFile, reportType, null);
        return new ReportResponse(reportType, file);
    }

    private String titleOr(ReportRequest request, String fallback) {
        return request != null && request.getTitle() != null && !request.getTitle().isBlank()
                ? request.getTitle().trim()
                : fallback;
    }

    private List<LessonResponse> filterLessons(List<LessonResponse> lessons, ReportRequest request) {
        if (request == null) {
            return lessons;
        }
        return lessons.stream()
                .filter(lesson -> request.getLessonIds() == null || request.getLessonIds().isEmpty()
                        || request.getLessonIds().contains(lesson.getId()))
                .filter(lesson -> inRange(lesson.getUpdatedAt() == null ? null : lesson.getUpdatedAt().toLocalDate(), request))
                .toList();
    }

    private List<ReflectionResponse> filterReflections(List<ReflectionResponse> reflections, ReportRequest request) {
        if (request == null) {
            return reflections;
        }
        return reflections.stream()
                .filter(reflection -> request.getReflectionIds() == null || request.getReflectionIds().isEmpty()
                        || request.getReflectionIds().contains(reflection.getId()))
                .filter(reflection -> inRange(reflection.getCreatedAt() == null ? null : reflection.getCreatedAt().toLocalDate(), request))
                .toList();
    }

    private List<ResearchTopicResponse> filterTopics(List<ResearchTopicResponse> topics, ReportRequest request) {
        if (request == null) {
            return topics;
        }
        return topics.stream()
                .filter(topic -> inRange(topic.getUpdatedAt() == null ? null : topic.getUpdatedAt().toLocalDate(), request))
                .toList();
    }

    private List<GrowthEventResponse> filterEvents(List<GrowthEventResponse> events, ReportRequest request) {
        if (request == null) {
            return events;
        }
        return events.stream()
                .filter(event -> inRange(event.getEventTime() == null ? null : event.getEventTime().toLocalDate(), request))
                .toList();
    }

    private boolean inRange(LocalDate date, ReportRequest request) {
        if (date == null || request == null) {
            return true;
        }
        if (request.getStartDate() != null && date.isBefore(request.getStartDate())) {
            return false;
        }
        return request.getEndDate() == null || !date.isAfter(request.getEndDate());
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
