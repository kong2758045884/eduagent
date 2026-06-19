package com.innovation.training.module.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovation.training.common.BusinessException;
import com.innovation.training.common.ErrorCode;
import com.innovation.training.module.ai.entity.AiCallLog;
import com.innovation.training.module.ai.mapper.AiCallLogMapper;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Component
public class QwenChatClient {

    private final QwenProperties properties;
    private final RestClient restClient;
    private final AiCallLogMapper aiCallLogMapper;
    private final ObjectMapper objectMapper;

    public QwenChatClient(QwenProperties properties, AiCallLogMapper aiCallLogMapper, ObjectMapper objectMapper) {
        this.properties = properties;
        this.aiCallLogMapper = aiCallLogMapper;
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder()
                .requestFactory(ClientHttpRequestFactories.get(ClientHttpRequestFactorySettings.DEFAULTS
                        .withConnectTimeout(Duration.ofSeconds(properties.getTimeoutSeconds()))
                        .withReadTimeout(Duration.ofSeconds(properties.getTimeoutSeconds()))))
                .build();
    }

    public void chatStream(String systemPrompt, List<Map<String, String>> conversation,
                           Consumer<String> onChunk) {
        if (!StringUtils.hasText(properties.getApiKey())) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "未配置 DASHSCOPE_API_KEY，无法调用千问模型");
        }

        List<Map<String, String>> messages = new java.util.ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt));
        messages.addAll(conversation);
        Map<String, Object> request = Map.of(
                "model", properties.getTextModel(),
                "temperature", properties.getTemperature(),
                "stream", true,
                "messages", messages
        );
        int promptChars = messages.stream()
                .mapToInt(message -> message.getOrDefault("content", "").length())
                .sum();
        long start = System.currentTimeMillis();
        StringBuilder fullContent = new StringBuilder();

        try {
            restClient.post()
                    .uri(properties.getBaseUrl() + "/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getApiKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.TEXT_EVENT_STREAM)
                    .body(request)
                    .exchange((clientRequest, response) -> {
                        if (!response.getStatusCode().is2xxSuccessful()) {
                            throw new BusinessException(ErrorCode.INTERNAL_ERROR,
                                    qwenErrorMessage(response.getStatusCode().value(), response.getBody()));
                        }
                        readStream(response.getBody(), fullContent, onChunk);
                        return null;
                    });
            logCall("avatar-chat-stream", properties.getTextModel(), promptChars,
                    fullContent.length(), System.currentTimeMillis() - start, true, null);
        } catch (RuntimeException ex) {
            logCall("avatar-chat-stream", properties.getTextModel(), promptChars,
                    fullContent.length(), System.currentTimeMillis() - start, false, ex.getMessage());
            throw ex;
        }
    }

    private void readStream(java.io.InputStream inputStream, StringBuilder fullContent,
                            Consumer<String> onChunk) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("data:")) {
                    continue;
                }
                String data = line.substring(5).trim();
                if (data.isEmpty() || "[DONE]".equals(data)) {
                    continue;
                }
                JsonNode root = objectMapper.readTree(data);
                JsonNode content = root.path("choices").path(0).path("delta").path("content");
                if (content.isTextual() && !content.asText().isEmpty()) {
                    String chunk = content.asText();
                    fullContent.append(chunk);
                    onChunk.accept(chunk);
                }
            }
        } catch (IOException ex) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "读取千问流式响应失败");
        }
    }

    private String qwenErrorMessage(int status, java.io.InputStream inputStream) {
        String prefix = "千问模型请求失败（" + status + "）";
        try {
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            if (!StringUtils.hasText(body)) {
                return prefix;
            }
            JsonNode root = objectMapper.readTree(body);
            JsonNode error = root.path("error");
            String message = error.path("message").asText("");
            String type = error.path("type").asText("");
            if (!StringUtils.hasText(message)) {
                message = root.path("message").asText("");
            }
            if (!StringUtils.hasText(message)) {
                return prefix;
            }
            return StringUtils.hasText(type)
                    ? prefix + "：" + message + " [" + type + "]"
                    : prefix + "：" + message;
        } catch (IOException ignored) {
            return prefix;
        }
    }

    public String chat(String systemPrompt, String userPrompt) {
        if (!StringUtils.hasText(properties.getApiKey())) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "未配置 DASHSCOPE_API_KEY，无法调用千问模型");
        }

        long start = System.currentTimeMillis();
        Map<String, Object> request = Map.of(
                "model", properties.getTextModel(),
                "temperature", properties.getTemperature(),
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                )
        );

        try {
            Map<?, ?> response = restClient.post()
                    .uri(properties.getBaseUrl() + "/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getApiKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(Map.class);
            String content = extractContent(response);
            logCall("chat", properties.getTextModel(), systemPrompt.length() + userPrompt.length(),
                    content.length(), System.currentTimeMillis() - start, true, null);
            return content;
        } catch (RuntimeException ex) {
            logCall("chat", properties.getTextModel(), systemPrompt.length() + userPrompt.length(),
                    0, System.currentTimeMillis() - start, false, ex.getMessage());
            throw ex;
        }
    }

    public String chatWithImage(String systemPrompt, String userPrompt, byte[] imageBytes, String contentType) {
        if (!StringUtils.hasText(properties.getApiKey())) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "未配置 DASHSCOPE_API_KEY，无法调用千问模型");
        }
        if (imageBytes == null || imageBytes.length == 0) {
            return chat(systemPrompt, userPrompt);
        }

        String mimeType = StringUtils.hasText(contentType) ? contentType : "image/jpeg";
        String dataUrl = "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(imageBytes);
        long start = System.currentTimeMillis();
        Map<String, Object> request = Map.of(
                "model", properties.getVisionModel(),
                "temperature", properties.getTemperature(),
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", List.of(
                                Map.of("type", "text", "text", userPrompt),
                                Map.of("type", "image_url", "image_url", Map.of("url", dataUrl))
                        ))
                )
        );

        try {
            Map<?, ?> response = restClient.post()
                    .uri(properties.getBaseUrl() + "/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getApiKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(Map.class);
            String content = extractContent(response);
            logCall("vision", properties.getVisionModel(), systemPrompt.length() + userPrompt.length(),
                    content.length(), System.currentTimeMillis() - start, true, null);
            return content;
        } catch (RuntimeException ex) {
            logCall("vision", properties.getVisionModel(), systemPrompt.length() + userPrompt.length(),
                    0, System.currentTimeMillis() - start, false, ex.getMessage());
            throw ex;
        }
    }

    private void logCall(String scenario, String model, int promptChars, int responseChars,
                         long elapsedMs, boolean success, String errorMessage) {
        try {
            AiCallLog log = new AiCallLog();
            log.setScenario(scenario);
            log.setModel(model);
            log.setPromptChars(promptChars);
            log.setResponseChars(responseChars);
            log.setElapsedMs(elapsedMs);
            log.setSuccess(success ? 1 : 0);
            log.setErrorMessage(errorMessage == null ? null : errorMessage.substring(0, Math.min(500, errorMessage.length())));
            log.setCreatedAt(LocalDateTime.now());
            aiCallLogMapper.insert(log);
        } catch (RuntimeException ignored) {
            // AI 日志不能影响主业务。
        }
    }

    @SuppressWarnings("unchecked")
    private String extractContent(Map<?, ?> response) {
        if (response == null) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "千问模型返回为空");
        }
        Object choicesObject = response.get("choices");
        if (!(choicesObject instanceof List<?> choices) || choices.isEmpty()) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "千问模型返回格式异常");
        }
        Object firstChoice = choices.get(0);
        if (!(firstChoice instanceof Map<?, ?> choice)) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "千问模型返回格式异常");
        }
        Object messageObject = choice.get("message");
        if (!(messageObject instanceof Map<?, ?> message)) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "千问模型返回格式异常");
        }
        Object content = message.get("content");
        if (!(content instanceof String text) || !StringUtils.hasText(text)) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "千问模型未返回有效内容");
        }
        return text.trim();
    }
}
