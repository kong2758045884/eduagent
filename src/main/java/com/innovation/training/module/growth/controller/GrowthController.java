package com.innovation.training.module.growth.controller;

import com.innovation.training.common.Result;
import com.innovation.training.module.growth.dto.CreateGrowthEventRequest;
import com.innovation.training.module.growth.dto.CreateGrowthFeedbackRequest;
import com.innovation.training.module.growth.dto.GrowthEventResponse;
import com.innovation.training.module.growth.dto.GrowthFeedbackResponse;
import com.innovation.training.module.growth.dto.GrowthPortfolioResponse;
import com.innovation.training.module.growth.service.GrowthService;
import com.innovation.training.support.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "成长档案袋")
@RestController
@RequestMapping("/api/growth/events")
public class GrowthController {

    private final GrowthService growthService;
    private final CurrentUserService currentUserService;

    public GrowthController(GrowthService growthService, CurrentUserService currentUserService) {
        this.growthService = growthService;
        this.currentUserService = currentUserService;
    }

    @Operation(summary = "新增成长事件")
    @PostMapping
    public Result<GrowthEventResponse> create(@Valid @RequestBody CreateGrowthEventRequest request,
                                              Authentication authentication) {
        return Result.success(growthService.create(currentUserService.requireUserId(authentication), request));
    }

    @Operation(summary = "获取成长时间线")
    @GetMapping
    public Result<List<GrowthEventResponse>> list(Authentication authentication) {
        return Result.success(growthService.list(currentUserService.requireUserId(authentication)));
    }

    @Operation(summary = "获取成长档案袋统计")
    @GetMapping("/portfolio")
    public Result<GrowthPortfolioResponse> portfolio(Authentication authentication) {
        return Result.success(growthService.portfolio(currentUserService.requireUserId(authentication)));
    }

    @Operation(summary = "新增成长档案点评/反馈/方法转化案例")
    @PostMapping("/feedback")
    public Result<GrowthFeedbackResponse> createFeedback(@Valid @RequestBody CreateGrowthFeedbackRequest request,
                                                        Authentication authentication) {
        return Result.success(growthService.createFeedback(currentUserService.requireUserId(authentication), request));
    }

    @Operation(summary = "查询成长档案点评/反馈/方法转化案例")
    @GetMapping("/feedback")
    public Result<List<GrowthFeedbackResponse>> listFeedback(@org.springframework.web.bind.annotation.RequestParam(required = false) String feedbackType,
                                                            Authentication authentication) {
        return Result.success(growthService.listFeedback(currentUserService.requireUserId(authentication), feedbackType));
    }
}
