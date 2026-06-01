package com.innovation.training.module.speech.controller;

import com.innovation.training.common.Result;
import com.innovation.training.module.speech.dto.SpeechTranscriptionResponse;
import com.innovation.training.module.speech.service.SpeechService;
import com.innovation.training.support.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "语音识别")
@RestController
@RequestMapping("/api/speech")
public class SpeechController {

    private final SpeechService speechService;
    private final CurrentUserService currentUserService;

    public SpeechController(SpeechService speechService, CurrentUserService currentUserService) {
        this.speechService = speechService;
        this.currentUserService = currentUserService;
    }

    @Operation(summary = "上传语音音频并调用千问转写")
    @PostMapping("/transcribe")
    public Result<SpeechTranscriptionResponse> transcribe(@RequestParam("file") MultipartFile file,
                                                          @RequestParam(required = false, defaultValue = "zh-CN") String language,
                                                          @RequestParam(required = false) String bizType,
                                                          @RequestParam(required = false) Long bizId,
                                                          Authentication authentication) {
        return Result.success(speechService.transcribe(
                currentUserService.requireUserId(authentication), file, language, bizType, bizId));
    }
}
