package com.innovation.training.module.diagnosis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovation.training.common.BusinessException;
import com.innovation.training.common.ErrorCode;
import com.innovation.training.module.ai.AiGenerationService;
import com.innovation.training.module.ai.QwenOcrClient;
import com.innovation.training.module.diagnosis.dto.CreateDiagnosisRequest;
import com.innovation.training.module.diagnosis.dto.DiagnosisResponse;
import com.innovation.training.module.diagnosis.entity.DiagnosisReport;
import com.innovation.training.module.diagnosis.mapper.DiagnosisReportMapper;
import com.innovation.training.module.diagnosis.service.ClassHeatmapResponse;
import com.innovation.training.module.diagnosis.service.DiagnosisService;
import com.innovation.training.module.diagnosis.service.StudentDiagnosisProfileResponse;
import com.innovation.training.module.diagnosis.service.StudentDiagnosisSummaryResponse;
import com.innovation.training.module.growth.service.GrowthService;
import com.innovation.training.support.StoredFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;

@Service
public class DiagnosisServiceImpl implements DiagnosisService {

    private final DiagnosisReportMapper diagnosisReportMapper;
    private final AiGenerationService aiGenerationService;
    private final ObjectMapper objectMapper;
    private final GrowthService growthService;
    private final QwenOcrClient qwenOcrClient;

    public DiagnosisServiceImpl(DiagnosisReportMapper diagnosisReportMapper,
                                AiGenerationService aiGenerationService,
                                ObjectMapper objectMapper,
                                GrowthService growthService,
                                QwenOcrClient qwenOcrClient) {
        this.diagnosisReportMapper = diagnosisReportMapper;
        this.aiGenerationService = aiGenerationService;
        this.objectMapper = objectMapper;
        this.growthService = growthService;
        this.qwenOcrClient = qwenOcrClient;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DiagnosisResponse create(Long userId, CreateDiagnosisRequest request) {
        if (!StringUtils.hasText(request.getQuestionText()) && !StringUtils.hasText(request.getAnswerText())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "请至少提供题目文本或学生答案；如需图片识别请调用上传接口");
        }
        String reportJson = stripCodeFence(aiGenerationService.diagnoseMistake(
                request.getQuestionText(),
                request.getAnswerText(),
                request.getImageNote()));
        DiagnosisReport report = buildReport(userId, request, reportJson, request.getImageUrl());
        diagnosisReportMapper.insert(report);
        return DiagnosisResponse.from(report);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DiagnosisResponse createWithImage(Long userId, CreateDiagnosisRequest request, StoredFile image) {
        String recognizedText = qwenOcrClient.recognizeWorksheet(image.getBytes(), image.getContentType());
        String questionText = mergeText(request.getQuestionText(), "OCR识别内容：\n" + recognizedText);
        request.setQuestionText(questionText);
        request.setImageNote(mergeText(request.getImageNote(), "图片已通过千问 OCR 识别"));
        String reportJson = stripCodeFence(aiGenerationService.diagnoseMistake(
                request.getQuestionText(),
                request.getAnswerText(),
                request.getImageNote()));
        DiagnosisReport report = buildReport(userId, request, reportJson, image.getPublicUrl());
        diagnosisReportMapper.insert(report);
        return DiagnosisResponse.from(report);
    }

    private DiagnosisReport buildReport(Long userId, CreateDiagnosisRequest request, String reportJson, String imageUrl) {
        DiagnosisReport report = new DiagnosisReport();
        report.setUserId(userId);
        report.setStudentName(request.getStudentName());
        report.setClassName(request.getClassName());
        report.setSubject(request.getSubject());
        report.setTopic(request.getTopic());
        report.setQuestionText(request.getQuestionText());
        report.setAnswerText(request.getAnswerText());
        report.setImageUrl(imageUrl);
        report.setReportJson(reportJson);
        report.setRootCause(readText(reportJson, "rootCause"));
        report.setInterventions(readArray(reportJson, "interventions"));
        report.setCreatedAt(LocalDateTime.now());
        return report;
    }

    @Override
    public List<DiagnosisResponse> list(Long userId) {
        return diagnosisReportMapper.selectList(new LambdaQueryWrapper<DiagnosisReport>()
                        .eq(DiagnosisReport::getUserId, userId)
                        .orderByDesc(DiagnosisReport::getCreatedAt))
                .stream()
                .map(DiagnosisResponse::from)
                .toList();
    }

    @Override
    public List<StudentDiagnosisSummaryResponse> listStudents(Long userId, String className) {
        return buildStudentSummaries(loadReports(userId, className, null));
    }

    @Override
    public StudentDiagnosisProfileResponse studentProfile(Long userId, String studentName, String className) {
        if (!StringUtils.hasText(studentName)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "学生姓名不能为空");
        }
        List<DiagnosisReport> reports = loadReports(userId, className, studentName);
        if (reports.isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "学生诊断档案不存在");
        }
        StudentDiagnosisProfileResponse response = new StudentDiagnosisProfileResponse();
        response.setSummary(buildSummary(reports.get(0), reports.size()));
        response.setReports(reports.stream().map(DiagnosisResponse::from).toList());
        response.setTopicDistribution(reports.stream()
                .collect(Collectors.groupingBy(report -> defaultTopic(report.getTopic()), LinkedHashMap::new, Collectors.counting())));
        return response;
    }

    @Override
    public ClassHeatmapResponse heatmap(Long userId, String className, Integer days) {
        LambdaQueryWrapper<DiagnosisReport> wrapper = new LambdaQueryWrapper<DiagnosisReport>()
                .eq(DiagnosisReport::getUserId, userId)
                .orderByDesc(DiagnosisReport::getCreatedAt);
        if (StringUtils.hasText(className)) {
            wrapper.eq(DiagnosisReport::getClassName, className.trim());
        }
        if (days != null && days > 0) {
            wrapper.ge(DiagnosisReport::getCreatedAt, LocalDateTime.now().minusDays(days));
        }
        List<DiagnosisReport> reports = diagnosisReportMapper.selectList(wrapper);
        List<StudentDiagnosisSummaryResponse> students = buildStudentSummaries(reports);
        List<ClassHeatmapResponse.HeatmapTopicItem> topics = reports.stream()
                .collect(Collectors.groupingBy(report -> defaultTopic(report.getTopic()), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> new ClassHeatmapResponse.HeatmapTopicItem(entry.getKey(), entry.getValue(), heatLevel(entry.getValue())))
                .toList();
        ClassHeatmapResponse response = new ClassHeatmapResponse();
        response.setClassName(StringUtils.hasText(className) ? className.trim() : "全部班级");
        response.setTotalReports((long) reports.size());
        response.setHighRiskStudents(students.stream().filter(item -> "high".equals(item.getRiskLevel())).count());
        response.setTopics(topics);
        response.setStudents(students);
        return response;
    }

    @Override
    public DiagnosisResponse archive(Long userId, Long diagnosisId, String note) {
        DiagnosisReport report = diagnosisReportMapper.selectOne(new LambdaQueryWrapper<DiagnosisReport>()
                .eq(DiagnosisReport::getId, diagnosisId)
                .eq(DiagnosisReport::getUserId, userId)
                .last("LIMIT 1"));
        if (report == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "诊断报告不存在");
        }
        String title = "归档学生诊断：" + defaultText(report.getStudentName(), "未命名学生");
        String content = StringUtils.hasText(note) ? note.trim() : defaultText(report.getRootCause(), report.getTopic());
        growthService.record(userId, "diagnosis_archived", title, content, "diagnosis", report.getId());
        return DiagnosisResponse.from(report);
    }

    private List<DiagnosisReport> loadReports(Long userId, String className, String studentName) {
        LambdaQueryWrapper<DiagnosisReport> wrapper = new LambdaQueryWrapper<DiagnosisReport>()
                .eq(DiagnosisReport::getUserId, userId)
                .orderByDesc(DiagnosisReport::getCreatedAt);
        if (StringUtils.hasText(className)) {
            wrapper.eq(DiagnosisReport::getClassName, className.trim());
        }
        if (StringUtils.hasText(studentName)) {
            wrapper.eq(DiagnosisReport::getStudentName, studentName.trim());
        }
        return diagnosisReportMapper.selectList(wrapper);
    }

    private List<StudentDiagnosisSummaryResponse> buildStudentSummaries(List<DiagnosisReport> reports) {
        return reports.stream()
                .filter(report -> StringUtils.hasText(report.getStudentName()))
                .collect(Collectors.groupingBy(this::studentKey, LinkedHashMap::new, Collectors.toList()))
                .values()
                .stream()
                .map(items -> {
                    List<DiagnosisReport> sorted = items.stream()
                            .sorted(Comparator.comparing(DiagnosisReport::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                            .toList();
                    return buildSummary(sorted.get(0), sorted.size());
                })
                .sorted(Comparator.comparing(StudentDiagnosisSummaryResponse::getReportCount).reversed())
                .toList();
    }

    private StudentDiagnosisSummaryResponse buildSummary(DiagnosisReport latest, long count) {
        StudentDiagnosisSummaryResponse response = new StudentDiagnosisSummaryResponse();
        response.setStudentName(latest.getStudentName());
        response.setClassName(latest.getClassName());
        response.setSubject(latest.getSubject());
        response.setLatestTopic(latest.getTopic());
        response.setLatestRootCause(latest.getRootCause());
        response.setLatestInterventions(latest.getInterventions());
        response.setReportCount(count);
        response.setRiskLevel(riskLevel(count));
        response.setLatestAt(latest.getCreatedAt());
        return response;
    }

    private String studentKey(DiagnosisReport report) {
        return defaultText(report.getClassName(), "") + "|" + report.getStudentName();
    }

    private String riskLevel(long count) {
        if (count >= 8) {
            return "high";
        }
        if (count >= 4) {
            return "medium";
        }
        return "low";
    }

    private String heatLevel(long count) {
        if (count >= 10) {
            return "high";
        }
        if (count >= 4) {
            return "medium";
        }
        return "low";
    }

    private String defaultTopic(String topic) {
        return defaultText(topic, "未分类知识点");
    }

    private String defaultText(String value, String fallback) {
        return StringUtils.hasText(value) ? value.trim() : fallback;
    }

    private String mergeText(String primary, String extra) {
        if (!StringUtils.hasText(primary)) {
            return extra;
        }
        if (!StringUtils.hasText(extra)) {
            return primary.trim();
        }
        return primary.trim() + "\n" + extra.trim();
    }

    private String readText(String json, String field) {
        try {
            return objectMapper.readTree(json).path(field).asText();
        } catch (Exception ex) {
            return null;
        }
    }

    private String readArray(String json, String field) {
        try {
            JsonNode array = objectMapper.readTree(json).path(field);
            if (!array.isArray()) {
                return null;
            }
            return StreamSupport.stream(array.spliterator(), false)
                    .map(JsonNode::asText)
                    .collect(Collectors.joining("\n"));
        } catch (Exception ex) {
            return null;
        }
    }

    private String stripCodeFence(String value) {
        String text = value == null ? "" : value.trim();
        if (text.startsWith("```")) {
            text = text.replaceFirst("^```(?:json)?", "").replaceFirst("```$", "").trim();
        }
        return text;
    }
}
