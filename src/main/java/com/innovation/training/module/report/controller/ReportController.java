package com.innovation.training.module.report.controller;

import com.innovation.training.common.Result;
import com.innovation.training.module.report.dto.ReportRequest;
import com.innovation.training.module.report.dto.ReportResponse;
import com.innovation.training.module.report.service.ReportService;
import com.innovation.training.support.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "报告导出")
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    private final CurrentUserService currentUserService;

    public ReportController(ReportService reportService, CurrentUserService currentUserService) {
        this.reportService = reportService;
        this.currentUserService = currentUserService;
    }

    @Operation(summary = "生成老教师教学经验册 PDF")
    @PostMapping("/experience-book")
    public Result<ReportResponse> experienceBook(@RequestBody(required = false) ReportRequest request,
                                                 Authentication authentication) {
        return Result.success(reportService.experienceBook(currentUserService.requireUserId(authentication),
                request == null ? new ReportRequest() : request));
    }

    @Operation(summary = "生成中年教师考核材料 Word")
    @PostMapping("/assessment-package")
    public Result<ReportResponse> assessmentPackage(@RequestBody(required = false) ReportRequest request,
                                                    Authentication authentication) {
        return Result.success(reportService.assessmentPackage(currentUserService.requireUserId(authentication),
                request == null ? new ReportRequest() : request));
    }

    @Operation(summary = "生成新任教师成长报告 PDF")
    @PostMapping("/growth-report")
    public Result<ReportResponse> growthReport(@RequestBody(required = false) ReportRequest request,
                                               Authentication authentication) {
        return Result.success(reportService.growthReport(currentUserService.requireUserId(authentication),
                request == null ? new ReportRequest() : request));
    }
}
