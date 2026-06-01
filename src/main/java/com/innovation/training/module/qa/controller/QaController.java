package com.innovation.training.module.qa.controller;

import com.innovation.training.common.Result;
import com.innovation.training.module.qa.dto.CreateQuestionRequest;
import com.innovation.training.module.qa.dto.CreateReplyRequest;
import com.innovation.training.module.qa.dto.QaQuestionResponse;
import com.innovation.training.module.qa.dto.QaReplyResponse;
import com.innovation.training.module.qa.service.QaService;
import com.innovation.training.support.CurrentUserService;
import com.innovation.training.support.TeacherAccessService;
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

@Tag(name = "在线答疑")
@RestController
@RequestMapping("/api/qa/questions")
public class QaController {

    private final QaService qaService;
    private final CurrentUserService currentUserService;
    private final TeacherAccessService teacherAccessService;

    public QaController(QaService qaService, CurrentUserService currentUserService,
                        TeacherAccessService teacherAccessService) {
        this.qaService = qaService;
        this.currentUserService = currentUserService;
        this.teacherAccessService = teacherAccessService;
    }

    @Operation(summary = "发布课堂答疑问题")
    @PostMapping
    public Result<QaQuestionResponse> create(@Valid @RequestBody CreateQuestionRequest request,
                                             Authentication authentication) {
        teacherAccessService.requireAny(authentication, "novice");
        return Result.success(qaService.create(currentUserService.requireUserId(authentication), request));
    }

    @Operation(summary = "查询答疑动态")
    @GetMapping
    public Result<List<QaQuestionResponse>> list(@RequestParam(required = false, defaultValue = "false") Boolean mineOnly,
                                                 Authentication authentication) {
        return Result.success(qaService.list(currentUserService.requireUserId(authentication), mineOnly));
    }

    @Operation(summary = "回复答疑问题")
    @PostMapping("/{id}/replies")
    public Result<QaReplyResponse> reply(@PathVariable Long id,
                                         @Valid @RequestBody CreateReplyRequest request,
                                         Authentication authentication) {
        return Result.success(qaService.reply(currentUserService.requireUserId(authentication), id, request));
    }

    @Operation(summary = "转发问题给名师")
    @PostMapping("/{id}/forward")
    public Result<QaQuestionResponse> forward(@PathVariable Long id,
                                              @RequestParam(required = false) Long mentorUserId,
                                              Authentication authentication) {
        return Result.success(qaService.forward(currentUserService.requireUserId(authentication), id, mentorUserId));
    }
}
