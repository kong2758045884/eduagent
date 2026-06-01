package com.innovation.training.module.profile.controller;

import com.innovation.training.common.Result;
import com.innovation.training.module.profile.dto.ProfileDashboardResponse;
import com.innovation.training.module.profile.dto.UpdateProfileRequest;
import com.innovation.training.module.profile.service.ProfileService;
import com.innovation.training.module.user.dto.UserResponse;
import com.innovation.training.module.user.entity.User;
import com.innovation.training.support.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "个人档案")
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final CurrentUserService currentUserService;
    private final ProfileService profileService;

    public ProfileController(CurrentUserService currentUserService, ProfileService profileService) {
        this.currentUserService = currentUserService;
        this.profileService = profileService;
    }

    @Operation(summary = "获取当前用户个人档案")
    @GetMapping
    public Result<UserResponse> profile(Authentication authentication) {
        return Result.success(UserResponse.from(currentUserService.requireUser(authentication)));
    }

    @Operation(summary = "更新当前用户个人档案")
    @PutMapping
    public Result<UserResponse> update(@RequestBody UpdateProfileRequest request,
                                       Authentication authentication) {
        User user = currentUserService.requireUser(authentication);
        return Result.success(profileService.update(user, request));
    }

    @Operation(summary = "获取个人工作台统计")
    @GetMapping("/dashboard")
    public Result<ProfileDashboardResponse> dashboard(Authentication authentication) {
        return Result.success(profileService.dashboard(currentUserService.requireUser(authentication)));
    }
}
