package com.innovation.training.module.ai;

import com.innovation.training.module.ai.dto.AvatarChatRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "AI 教学助手")
@RestController
@RequestMapping("/api/ai/avatar")
public class AvatarChatController {

    private final AvatarChatService avatarChatService;

    public AvatarChatController(AvatarChatService avatarChatService) {
        this.avatarChatService = avatarChatService;
    }

    @Operation(summary = "流式生成 AI 教学助手回答")
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> stream(@Valid @RequestBody AvatarChatRequest request) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-transform")
                .header("X-Accel-Buffering", "no")
                .body(avatarChatService.stream(request));
    }
}
