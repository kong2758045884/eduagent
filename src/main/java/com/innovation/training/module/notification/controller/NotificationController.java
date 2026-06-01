package com.innovation.training.module.notification.controller;

import com.innovation.training.common.Result;
import com.innovation.training.module.notification.dto.NotificationResponse;
import com.innovation.training.module.notification.service.NotificationService;
import com.innovation.training.support.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "通知中心")
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final CurrentUserService currentUserService;

    public NotificationController(NotificationService notificationService, CurrentUserService currentUserService) {
        this.notificationService = notificationService;
        this.currentUserService = currentUserService;
    }

    @Operation(summary = "查询当前用户通知")
    @GetMapping
    public Result<List<NotificationResponse>> list(@RequestParam(required = false, defaultValue = "false") Boolean unreadOnly,
                                                   Authentication authentication) {
        return Result.success(notificationService.list(currentUserService.requireUserId(authentication), unreadOnly));
    }

    @Operation(summary = "标记通知已读")
    @PostMapping("/{id}/read")
    public Result<NotificationResponse> read(@PathVariable Long id, Authentication authentication) {
        return Result.success(notificationService.markRead(currentUserService.requireUserId(authentication), id));
    }
}
