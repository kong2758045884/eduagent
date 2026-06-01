package com.innovation.training.module.course.controller;

import com.innovation.training.common.Result;
import com.innovation.training.module.course.dto.CourseResponse;
import com.innovation.training.module.course.dto.UpdateCourseProgressRequest;
import com.innovation.training.module.course.service.TrainingCourseService;
import com.innovation.training.support.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "配套课程体系")
@RestController
@RequestMapping("/api/courses")
public class TrainingCourseController {

    private final TrainingCourseService trainingCourseService;
    private final CurrentUserService currentUserService;

    public TrainingCourseController(TrainingCourseService trainingCourseService,
                                    CurrentUserService currentUserService) {
        this.trainingCourseService = trainingCourseService;
        this.currentUserService = currentUserService;
    }

    @Operation(summary = "查询配套课程")
    @GetMapping
    public Result<List<CourseResponse>> list(@RequestParam(required = false) String audience,
                                             Authentication authentication) {
        return Result.success(trainingCourseService.list(currentUserService.requireUserId(authentication), audience));
    }

    @Operation(summary = "报名课程")
    @PostMapping("/{id}/enroll")
    public Result<CourseResponse> enroll(@PathVariable Long id, Authentication authentication) {
        return Result.success(trainingCourseService.enroll(currentUserService.requireUserId(authentication), id));
    }

    @Operation(summary = "更新课程进度和反馈")
    @PostMapping("/{id}/progress")
    public Result<CourseResponse> progress(@PathVariable Long id,
                                           @RequestBody UpdateCourseProgressRequest request,
                                           Authentication authentication) {
        return Result.success(trainingCourseService.updateProgress(currentUserService.requireUserId(authentication), id, request));
    }
}
