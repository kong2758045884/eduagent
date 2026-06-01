package com.innovation.training.module.library.controller;

import com.innovation.training.common.Result;
import com.innovation.training.module.library.service.TeachingLibraryService;
import com.innovation.training.module.resource.dto.ResourceResponse;
import com.innovation.training.module.user.entity.User;
import com.innovation.training.support.CurrentUserService;
import com.innovation.training.support.TeacherAccessService;
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

@Tag(name = "本土教法库")
@RestController
@RequestMapping("/api/teaching-library")
public class TeachingLibraryController {

    private final TeachingLibraryService teachingLibraryService;
    private final CurrentUserService currentUserService;
    private final TeacherAccessService teacherAccessService;

    public TeachingLibraryController(TeachingLibraryService teachingLibraryService,
                                     CurrentUserService currentUserService,
                                     TeacherAccessService teacherAccessService) {
        this.teachingLibraryService = teachingLibraryService;
        this.currentUserService = currentUserService;
        this.teacherAccessService = teacherAccessService;
    }

    @Operation(summary = "查询本县本土教法视频/经验")
    @GetMapping("/videos")
    public Result<List<ResourceResponse>> videos(@RequestParam(required = false) String tag,
                                                 Authentication authentication) {
        return Result.success(teachingLibraryService.videos(teacherAccessService.requireAny(authentication, "novice"), tag));
    }

    @Operation(summary = "记录收藏本土教法资源")
    @PostMapping("/videos/{id}/favorite")
    public Result<Void> favorite(@PathVariable Long id, Authentication authentication) {
        User user = currentUserService.requireUser(authentication);
        teacherAccessService.requireAny(authentication, "novice");
        teachingLibraryService.favorite(user, id);
        return Result.success();
    }

    @Operation(summary = "记录观看本土教法资源")
    @PostMapping("/videos/{id}/watched")
    public Result<Void> watched(@PathVariable Long id, Authentication authentication) {
        User user = currentUserService.requireUser(authentication);
        teacherAccessService.requireAny(authentication, "novice");
        teachingLibraryService.watched(user, id);
        return Result.success();
    }
}
