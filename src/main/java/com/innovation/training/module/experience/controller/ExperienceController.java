package com.innovation.training.module.experience.controller;

import com.innovation.training.common.Result;
import com.innovation.training.module.experience.dto.ExperienceBookResponse;
import com.innovation.training.module.experience.dto.ShareExperienceRequest;
import com.innovation.training.module.experience.service.ExperienceService;
import com.innovation.training.module.report.dto.ReportResponse;
import com.innovation.training.module.resource.dto.ResourceResponse;
import com.innovation.training.support.CurrentUserService;
import com.innovation.training.support.TeacherAccessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "经验沉淀助手")
@RestController
@RequestMapping("/api/experience")
public class ExperienceController {

    private final ExperienceService experienceService;
    private final CurrentUserService currentUserService;
    private final TeacherAccessService teacherAccessService;

    public ExperienceController(ExperienceService experienceService, CurrentUserService currentUserService,
                                TeacherAccessService teacherAccessService) {
        this.experienceService = experienceService;
        this.currentUserService = currentUserService;
        this.teacherAccessService = teacherAccessService;
    }

    @Operation(summary = "获取我的经验册数据")
    @GetMapping("/book")
    public Result<ExperienceBookResponse> book(Authentication authentication) {
        teacherAccessService.requireAny(authentication, "senior");
        return Result.success(experienceService.book(currentUserService.requireUserId(authentication)));
    }

    @Operation(summary = "分享经验到县域教研库")
    @PostMapping("/share")
    public Result<ResourceResponse> share(@Valid @RequestBody ShareExperienceRequest request,
                                          Authentication authentication) {
        return Result.success(experienceService.share(teacherAccessService.requireAny(authentication, "senior"), request));
    }

    @Operation(summary = "导出我的教学经验册 PDF")
    @PostMapping("/export")
    public Result<ReportResponse> export(@RequestParam(required = false) String title,
                                         Authentication authentication) {
        teacherAccessService.requireAny(authentication, "senior");
        return Result.success(experienceService.export(currentUserService.requireUserId(authentication), title));
    }
}
