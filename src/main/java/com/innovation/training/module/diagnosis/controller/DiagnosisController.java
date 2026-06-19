package com.innovation.training.module.diagnosis.controller;

import com.innovation.training.common.Result;
import com.innovation.training.module.diagnosis.dto.ArchiveDiagnosisRequest;
import com.innovation.training.module.diagnosis.dto.CreateDiagnosisRequest;
import com.innovation.training.module.diagnosis.dto.DiagnosisResponse;
import com.innovation.training.module.diagnosis.dto.DiagnosisTrendResponse;
import com.innovation.training.module.diagnosis.service.ClassHeatmapResponse;
import com.innovation.training.module.diagnosis.service.DiagnosisService;
import com.innovation.training.module.diagnosis.service.StudentDiagnosisProfileResponse;
import com.innovation.training.module.diagnosis.service.StudentDiagnosisSummaryResponse;
import com.innovation.training.support.CurrentUserService;
import com.innovation.training.support.LocalFileStorageService;
import com.innovation.training.support.StoredFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "学情诊断")
@RestController
@RequestMapping("/api/diagnoses")
public class DiagnosisController {

    private final DiagnosisService diagnosisService;
    private final CurrentUserService currentUserService;
    private final LocalFileStorageService fileStorageService;

    public DiagnosisController(DiagnosisService diagnosisService,
                               CurrentUserService currentUserService,
                               LocalFileStorageService fileStorageService) {
        this.diagnosisService = diagnosisService;
        this.currentUserService = currentUserService;
        this.fileStorageService = fileStorageService;
    }

    @Operation(summary = "生成并保存错题诊断")
    @PostMapping
    public Result<DiagnosisResponse> create(@Valid @RequestBody CreateDiagnosisRequest request,
                                            Authentication authentication) {
        return Result.success(diagnosisService.create(currentUserService.requireUserId(authentication), request));
    }

    @Operation(summary = "上传错题图片并调用视觉模型诊断")
    @PostMapping("/upload")
    public Result<DiagnosisResponse> upload(@RequestParam("file") MultipartFile file,
                                            @RequestParam(required = false) String studentName,
                                            @RequestParam(required = false) String className,
                                            @RequestParam(required = false, defaultValue = "数学") String subject,
                                            @RequestParam(required = false) String topic,
                                            @RequestParam(required = false) String questionText,
                                            @RequestParam(required = false) String answerText,
                                            @RequestParam(required = false) String imageNote,
                                            Authentication authentication) {
        StoredFile storedFile = fileStorageService.storeWithBytes(file, "diagnosis");
        CreateDiagnosisRequest request = new CreateDiagnosisRequest();
        request.setStudentName(studentName);
        request.setClassName(className);
        request.setSubject(subject);
        request.setTopic(topic);
        request.setQuestionText(questionText);
        request.setAnswerText(answerText);
        request.setImageNote(imageNote);
        request.setImageUrl(storedFile.getPublicUrl());
        return Result.success(diagnosisService.createWithImage(
                currentUserService.requireUserId(authentication), request, storedFile));
    }

    @Operation(summary = "获取当前用户诊断记录")
    @GetMapping
    public Result<List<DiagnosisResponse>> list(Authentication authentication) {
        return Result.success(diagnosisService.list(currentUserService.requireUserId(authentication)));
    }

    @Operation(summary = "按学生聚合诊断档案")
    @GetMapping("/students")
    public Result<List<StudentDiagnosisSummaryResponse>> students(@RequestParam(required = false) String className,
                                                                  Authentication authentication) {
        return Result.success(diagnosisService.listStudents(currentUserService.requireUserId(authentication), className));
    }

    @Operation(summary = "获取单个学生诊断档案")
    @GetMapping("/students/{studentName}")
    public Result<StudentDiagnosisProfileResponse> studentProfile(@PathVariable String studentName,
                                                                  @RequestParam(required = false) String className,
                                                                  Authentication authentication) {
        return Result.success(diagnosisService.studentProfile(
                currentUserService.requireUserId(authentication), studentName, className));
    }

    @Operation(summary = "生成班级错题热力图数据")
    @GetMapping("/heatmap")
    public Result<ClassHeatmapResponse> heatmap(@RequestParam(required = false) String className,
                                                @RequestParam(required = false, defaultValue = "180") Integer days,
                                                Authentication authentication) {
        return Result.success(diagnosisService.heatmap(currentUserService.requireUserId(authentication), className, days));
    }

    @Operation(summary = "获取近7日诊断趋势数据")
    @GetMapping("/trend")
    public Result<DiagnosisTrendResponse> trend(Authentication authentication) {
        return Result.success(diagnosisService.trend(currentUserService.requireUserId(authentication)));
    }

    @Operation(summary = "更新诊断报告")
    @PutMapping("/{id}")
    public Result<DiagnosisResponse> update(@PathVariable Long id,
                                             @Valid @RequestBody CreateDiagnosisRequest request,
                                             Authentication authentication) {
        return Result.success(diagnosisService.update(currentUserService.requireUserId(authentication), id, request));
    }

    @Operation(summary = "删除诊断报告")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, Authentication authentication) {
        diagnosisService.delete(currentUserService.requireUserId(authentication), id);
        return Result.success(null);
    }

    @Operation(summary = "归档诊断报告到成长档案")
    @PostMapping("/{id}/archive")
    public Result<DiagnosisResponse> archive(@PathVariable Long id,
                                             @RequestBody(required = false) ArchiveDiagnosisRequest request,
                                             Authentication authentication) {
        return Result.success(diagnosisService.archive(currentUserService.requireUserId(authentication), id,
                request == null ? null : request.getNote()));
    }
}
