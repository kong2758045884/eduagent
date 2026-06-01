package com.innovation.training.module.lesson.controller;

import com.innovation.training.common.Result;
import com.innovation.training.module.lesson.dto.CreateReflectionRequest;
import com.innovation.training.module.lesson.dto.GenerateLessonRequest;
import com.innovation.training.module.lesson.dto.GenerateLessonResponse;
import com.innovation.training.module.lesson.dto.LessonResponse;
import com.innovation.training.module.lesson.dto.ReflectionResponse;
import com.innovation.training.module.lesson.dto.SaveLessonRequest;
import com.innovation.training.module.lesson.service.LessonService;
import com.innovation.training.support.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "教案与反思")
@RestController
@RequestMapping("/api")
public class LessonController {

    private final LessonService lessonService;
    private final CurrentUserService currentUserService;

    public LessonController(LessonService lessonService, CurrentUserService currentUserService) {
        this.lessonService = lessonService;
        this.currentUserService = currentUserService;
    }

    @Operation(summary = "调用千问生成结构化教案")
    @PostMapping("/lessons/generate")
    public Result<GenerateLessonResponse> generate(@Valid @RequestBody GenerateLessonRequest request,
                                                   Authentication authentication) {
        return Result.success(lessonService.generate(currentUserService.requireUserId(authentication), request));
    }

    @Operation(summary = "保存教案")
    @PostMapping("/lessons")
    public Result<LessonResponse> save(@Valid @RequestBody SaveLessonRequest request,
                                       Authentication authentication) {
        return Result.success(lessonService.saveLesson(currentUserService.requireUserId(authentication), request));
    }

    @Operation(summary = "更新教案")
    @PutMapping("/lessons/{id}")
    public Result<LessonResponse> update(@PathVariable Long id,
                                         @Valid @RequestBody SaveLessonRequest request,
                                         Authentication authentication) {
        return Result.success(lessonService.updateLesson(currentUserService.requireUserId(authentication), id, request));
    }

    @Operation(summary = "获取当前用户教案列表")
    @GetMapping("/lessons")
    public Result<List<LessonResponse>> list(Authentication authentication) {
        return Result.success(lessonService.listLessons(currentUserService.requireUserId(authentication)));
    }

    @Operation(summary = "保存课后反思并关联教案")
    @PostMapping("/reflections")
    public Result<ReflectionResponse> createReflection(@Valid @RequestBody CreateReflectionRequest request,
                                                       Authentication authentication) {
        return Result.success(lessonService.createReflection(currentUserService.requireUserId(authentication), request));
    }

    @Operation(summary = "获取反思列表")
    @GetMapping("/reflections")
    public Result<List<ReflectionResponse>> listReflections(@RequestParam(required = false) Long lessonId,
                                                            Authentication authentication) {
        return Result.success(lessonService.listReflections(currentUserService.requireUserId(authentication), lessonId));
    }
}
