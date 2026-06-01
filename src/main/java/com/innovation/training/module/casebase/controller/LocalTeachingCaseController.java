package com.innovation.training.module.casebase.controller;

import com.innovation.training.common.Result;
import com.innovation.training.module.casebase.dto.CreateLocalTeachingCaseRequest;
import com.innovation.training.module.casebase.dto.LocalTeachingCaseResponse;
import com.innovation.training.module.casebase.service.LocalTeachingCaseService;
import com.innovation.training.support.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "乡土教学案例库")
@RestController
@RequestMapping("/api/local-cases")
public class LocalTeachingCaseController {

    private final LocalTeachingCaseService service;
    private final CurrentUserService currentUserService;

    public LocalTeachingCaseController(LocalTeachingCaseService service, CurrentUserService currentUserService) {
        this.service = service;
        this.currentUserService = currentUserService;
    }

    @Operation(summary = "查询乡土教学案例")
    @GetMapping
    public Result<List<LocalTeachingCaseResponse>> list(@RequestParam(required = false) String keyword,
                                                        @RequestParam(required = false) String subject,
                                                        @RequestParam(required = false) String grade) {
        return Result.success(service.list(keyword, subject, grade));
    }

    @Operation(summary = "新增乡土教学案例")
    @PostMapping
    public Result<LocalTeachingCaseResponse> create(@Valid @RequestBody CreateLocalTeachingCaseRequest request,
                                                   Authentication authentication) {
        return Result.success(service.create(request, currentUserService.requireUser(authentication)));
    }

    @Operation(summary = "初始化 mock 案例数据，仅空表时写入")
    @PostMapping("/seed")
    public Result<Void> seed() {
        service.seedIfEmpty();
        return Result.success();
    }
}
