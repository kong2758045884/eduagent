package com.innovation.training.module.file.controller;

import com.innovation.training.common.Result;
import com.innovation.training.module.file.dto.FileResponse;
import com.innovation.training.module.file.service.AppFileService;
import com.innovation.training.support.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "文件中心")
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final AppFileService appFileService;
    private final CurrentUserService currentUserService;

    public FileController(AppFileService appFileService, CurrentUserService currentUserService) {
        this.appFileService = appFileService;
        this.currentUserService = currentUserService;
    }

    @Operation(summary = "上传通用业务文件")
    @PostMapping
    public Result<FileResponse> upload(@RequestParam("file") MultipartFile file,
                                       @RequestParam(required = false) String bizType,
                                       @RequestParam(required = false) Long bizId,
                                       Authentication authentication) {
        return Result.success(appFileService.upload(currentUserService.requireUserId(authentication), file, bizType, bizId));
    }

    @Operation(summary = "查询当前用户文件")
    @GetMapping
    public Result<List<FileResponse>> list(@RequestParam(required = false) String bizType,
                                           @RequestParam(required = false) Long bizId,
                                           Authentication authentication) {
        return Result.success(appFileService.list(currentUserService.requireUserId(authentication), bizType, bizId));
    }
}
