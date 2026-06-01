package com.innovation.training.module.classroom;

import com.innovation.training.common.Result;
import com.innovation.training.support.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "课堂辅助资源包")
@RestController
@RequestMapping("/api/classroom/assets")
public class ClassroomAssetController {

    private final ClassroomAssetService classroomAssetService;
    private final CurrentUserService currentUserService;

    public ClassroomAssetController(ClassroomAssetService classroomAssetService, CurrentUserService currentUserService) {
        this.classroomAssetService = classroomAssetService;
        this.currentUserService = currentUserService;
    }

    @Operation(summary = "获取课堂辅助资源包列表")
    @GetMapping
    public Result<List<ClassroomAssetResponse>> list() {
        return Result.success(classroomAssetService.list());
    }

    @Operation(summary = "记录课堂辅助资源包使用")
    @PostMapping("/{assetId}/use")
    public Result<ClassroomAssetResponse> use(@PathVariable String assetId, Authentication authentication) {
        return Result.success(classroomAssetService.use(currentUserService.requireUserId(authentication), assetId));
    }
}
