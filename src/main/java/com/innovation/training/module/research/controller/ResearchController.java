package com.innovation.training.module.research.controller;

import com.innovation.training.common.Result;
import com.innovation.training.module.research.dto.RecommendTopicRequest;
import com.innovation.training.module.research.dto.RecommendTopicResponse;
import com.innovation.training.module.research.dto.ResearchTopicResponse;
import com.innovation.training.module.research.dto.SaveTopicRequest;
import com.innovation.training.module.research.service.ResearchService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "课题研究")
@RestController
@RequestMapping("/api/research/topics")
public class ResearchController {

    private final ResearchService researchService;
    private final CurrentUserService currentUserService;
    private final TeacherAccessService teacherAccessService;

    public ResearchController(ResearchService researchService, CurrentUserService currentUserService,
                              TeacherAccessService teacherAccessService) {
        this.researchService = researchService;
        this.currentUserService = currentUserService;
        this.teacherAccessService = teacherAccessService;
    }

    @Operation(summary = "调用千问推荐教研课题")
    @PostMapping("/recommend")
    public Result<RecommendTopicResponse> recommend(@RequestBody RecommendTopicRequest request,
                                                    Authentication authentication) {
        teacherAccessService.requireAny(authentication, "mid");
        return Result.success(researchService.recommend(currentUserService.requireUserId(authentication), request));
    }

    @Operation(summary = "保存课题")
    @PostMapping
    public Result<ResearchTopicResponse> save(@Valid @RequestBody SaveTopicRequest request,
                                              Authentication authentication) {
        teacherAccessService.requireAny(authentication, "mid");
        return Result.success(researchService.save(currentUserService.requireUserId(authentication), request));
    }

    @Operation(summary = "获取当前用户课题库")
    @GetMapping
    public Result<List<ResearchTopicResponse>> list(Authentication authentication) {
        teacherAccessService.requireAny(authentication, "mid");
        return Result.success(researchService.list(currentUserService.requireUserId(authentication)));
    }
}
