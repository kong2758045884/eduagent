package com.innovation.training.module.expert.controller;

import com.innovation.training.common.Result;
import com.innovation.training.module.expert.dto.AppointmentResponse;
import com.innovation.training.module.expert.dto.CreateAppointmentRequest;
import com.innovation.training.module.expert.dto.ExpertResponse;
import com.innovation.training.module.expert.dto.UpdateAppointmentRequest;
import com.innovation.training.module.expert.service.ExpertService;
import com.innovation.training.support.CurrentUserService;
import com.innovation.training.support.TeacherAccessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "专家智库")
@RestController
@RequestMapping("/api/experts")
public class ExpertController {

    private final ExpertService expertService;
    private final CurrentUserService currentUserService;
    private final TeacherAccessService teacherAccessService;

    public ExpertController(ExpertService expertService, CurrentUserService currentUserService,
                            TeacherAccessService teacherAccessService) {
        this.expertService = expertService;
        this.currentUserService = currentUserService;
        this.teacherAccessService = teacherAccessService;
    }

    @Operation(summary = "查询专家库")
    @GetMapping
    public Result<List<ExpertResponse>> list(@RequestParam(required = false) String field) {
        return Result.success(expertService.list(field));
    }

    @Operation(summary = "预约专家咨询")
    @PostMapping("/appointments")
    public Result<AppointmentResponse> createAppointment(@Valid @RequestBody CreateAppointmentRequest request,
                                                         Authentication authentication) {
        teacherAccessService.requireAny(authentication, "mid");
        return Result.success(expertService.createAppointment(currentUserService.requireUserId(authentication), request));
    }

    @Operation(summary = "查询我的专家咨询预约")
    @GetMapping("/appointments")
    public Result<List<AppointmentResponse>> listAppointments(Authentication authentication) {
        teacherAccessService.requireAny(authentication, "mid");
        return Result.success(expertService.listAppointments(currentUserService.requireUserId(authentication)));
    }

    @Operation(summary = "更新专家咨询预约状态/回复/会议链接")
    @PostMapping("/appointments/{id}")
    public Result<AppointmentResponse> updateAppointment(@PathVariable Long id,
                                                         @RequestBody UpdateAppointmentRequest request,
                                                         Authentication authentication) {
        teacherAccessService.requireAny(authentication, "mid");
        return Result.success(expertService.updateAppointment(currentUserService.requireUserId(authentication), id, request));
    }
}
