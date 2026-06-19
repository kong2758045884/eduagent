package com.innovation.training.module.ai;

import com.innovation.training.module.ai.dto.AvatarChatMessageRequest;
import com.innovation.training.module.ai.dto.AvatarChatRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class AvatarChatService {

    private final QwenChatClient qwenChatClient;
    private final QwenProperties properties;
    private final Executor streamExecutor;

    public AvatarChatService(QwenChatClient qwenChatClient, QwenProperties properties,
                             @Qualifier("aiStreamExecutor") Executor streamExecutor) {
        this.qwenChatClient = qwenChatClient;
        this.properties = properties;
        this.streamExecutor = streamExecutor;
    }

    public SseEmitter stream(AvatarChatRequest request) {
        SseEmitter emitter = new SseEmitter((properties.getTimeoutSeconds() + 15L) * 1000L);
        AtomicBoolean closed = new AtomicBoolean(false);
        emitter.onCompletion(() -> closed.set(true));
        emitter.onTimeout(() -> closed.set(true));
        emitter.onError(error -> closed.set(true));

        streamExecutor.execute(() -> runStream(request, emitter, closed));
        return emitter;
    }

    private void runStream(AvatarChatRequest request, SseEmitter emitter, AtomicBoolean closed) {
        try {
            qwenChatClient.chatStream(systemPrompt(request.getStyle()), messages(request), chunk -> {
                if (closed.get()) {
                    throw new ClientDisconnectedException();
                }
                send(emitter, "delta", Map.of("content", chunk));
            });
            if (!closed.get()) {
                send(emitter, "done", Map.of());
                emitter.complete();
            }
        } catch (ClientDisconnectedException ignored) {
            // 浏览器主动取消请求时，停止继续向已关闭的连接写数据。
        } catch (RuntimeException ex) {
            if (!closed.get()) {
                try {
                    send(emitter, "error", Map.of("message", safeMessage(ex)));
                    emitter.complete();
                } catch (ClientDisconnectedException ignored) {
                    // 错误返回前连接已关闭，无需再次处理。
                }
            }
        }
    }

    private List<Map<String, String>> messages(AvatarChatRequest request) {
        List<Map<String, String>> messages = new ArrayList<>();
        for (AvatarChatMessageRequest message : request.getHistory()) {
            String role = "ai".equals(message.getRole()) ? "assistant" : message.getRole();
            messages.add(Map.of("role", role, "content", message.getContent()));
        }
        messages.add(Map.of("role", "user", "content", request.getPrompt()));
        return messages;
    }

    private String systemPrompt(String style) {
        return """
                你是乡村数学教学智能助手，服务对象是乡村学校教师。
                你的任务是根据教师的教学问题，提供可直接用于课堂的讲解建议、互动方案和教学策略。
                回答要求：
                - 语言简洁亲切，像经验丰富的同事在交流
                - 结合乡村教学实际场景
                - 给出具体可操作的建议，不要空泛理论
                - 当前教师选择的教学风格是：%s，请据此调整回答的语气和侧重点
                """.formatted(style);
    }

    private void send(SseEmitter emitter, String event, Object data) {
        try {
            emitter.send(SseEmitter.event().name(event).data(data));
        } catch (IOException | IllegalStateException ex) {
            throw new ClientDisconnectedException();
        }
    }

    private String safeMessage(RuntimeException ex) {
        String message = ex.getMessage();
        return message == null || message.isBlank() ? "AI 回答生成失败" : message;
    }

    private static final class ClientDisconnectedException extends RuntimeException {
    }
}
