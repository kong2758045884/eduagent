package com.innovation.training.module.resource.controller;

import com.innovation.training.common.Result;
import com.innovation.training.module.resource.dto.CommentResponse;
import com.innovation.training.module.resource.dto.CreateCommentRequest;
import com.innovation.training.module.resource.dto.CreateResourceRequest;
import com.innovation.training.module.resource.dto.ResourceResponse;
import com.innovation.training.module.resource.service.ResourceService;
import com.innovation.training.support.CurrentUserService;
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

@Tag(name = "县域教研资源")
@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService resourceService;
    private final CurrentUserService currentUserService;

    public ResourceController(ResourceService resourceService, CurrentUserService currentUserService) {
        this.resourceService = resourceService;
        this.currentUserService = currentUserService;
    }

    @Operation(summary = "分享资源到县域教研库")
    @PostMapping
    public Result<ResourceResponse> create(@Valid @RequestBody CreateResourceRequest request,
                                           Authentication authentication) {
        return Result.success(resourceService.create(currentUserService.requireUserId(authentication), request));
    }

    @Operation(summary = "查询县域教研资源")
    @GetMapping
    public Result<List<ResourceResponse>> list(@RequestParam(required = false) String county,
                                               @RequestParam(required = false) String resourceType,
                                               @RequestParam(required = false) String subject,
                                               @RequestParam(required = false) String grade) {
        return Result.success(resourceService.list(county, resourceType, subject, grade));
    }

    @Operation(summary = "查询待审核资源")
    @GetMapping("/audit/pending")
    public Result<List<ResourceResponse>> pendingAudit() {
        return Result.success(resourceService.listPendingAudit());
    }

    @Operation(summary = "资源点赞")
    @PostMapping("/{id}/like")
    public Result<ResourceResponse> like(@PathVariable Long id) {
        return Result.success(resourceService.like(id));
    }

    @Operation(summary = "资源收藏")
    @PostMapping("/{id}/favorite")
    public Result<ResourceResponse> favorite(@PathVariable Long id, Authentication authentication) {
        return Result.success(resourceService.favorite(currentUserService.requireUserId(authentication), id));
    }

    @Operation(summary = "记录资源观看/浏览")
    @PostMapping("/{id}/watched")
    public Result<ResourceResponse> watched(@PathVariable Long id, Authentication authentication) {
        return Result.success(resourceService.watched(currentUserService.requireUserId(authentication), id));
    }

    @Operation(summary = "资源审核")
    @PostMapping("/{id}/review")
    public Result<ResourceResponse> review(@PathVariable Long id,
                                           @RequestParam(defaultValue = "approved") String auditStatus) {
        return Result.success(resourceService.review(id, auditStatus));
    }

    @Operation(summary = "新增资源评论")
    @PostMapping("/{id}/comments")
    public Result<CommentResponse> comment(@PathVariable Long id,
                                           @Valid @RequestBody CreateCommentRequest request,
                                           Authentication authentication) {
        return Result.success(resourceService.comment(currentUserService.requireUserId(authentication), id, request));
    }

    @Operation(summary = "查询资源评论")
    @GetMapping("/{id}/comments")
    public Result<List<CommentResponse>> listComments(@PathVariable Long id) {
        return Result.success(resourceService.listComments(id));
    }
}
