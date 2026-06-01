package com.innovation.training.module.ai;

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
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class QwenSpeechClient {

    private final QwenProperties properties;
    private final RestClient restClient;
    private final AiCallLogMapper aiCallLogMapper;

    public QwenSpeechClient(QwenProperties properties, AiCallLogMapper aiCallLogMapper) {
        this.properties = properties;
        this.aiCallLogMapper = aiCallLogMapper;
        this.restClient = RestClient.builder()
                .requestFactory(ClientHttpRequestFactories.get(ClientHttpRequestFactorySettings.DEFAULTS
                        .withConnectTimeout(Duration.ofSeconds(properties.getTimeoutSeconds()))
                        .withReadTimeout(Duration.ofSeconds(properties.getTimeoutSeconds()))))
                .build();
    }

    public String transcribe(byte[] audioBytes, String contentType, String languageHint) {
        if (!StringUtils.hasText(properties.getApiKey())) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "DASHSCOPE_API_KEY is not configured");
        }
        if (audioBytes == null || audioBytes.length == 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Audio content must not be empty");
        }

        String base64 = Base64.getEncoder().encodeToString(audioBytes);
        if (base64.length() > properties.getSpeechMaxBase64Bytes()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Audio is too large; keep Base64 payload under the configured limit");
        }

        String mimeType = StringUtils.hasText(contentType) ? contentType : "audio/wav";
        String dataUrl = "data:" + mimeType + ";base64," + base64;
        String language = normalizeLanguage(languageHint);

        Map<String, Object> asrOptions = new LinkedHashMap<>();
        asrOptions.put("enable_itn", false);
        if (StringUtils.hasText(language)) {
            asrOptions.put("language", language);
        }

        Map<String, Object> request = new LinkedHashMap<>();
        request.put("model", properties.getAudioModel());
        request.put("messages", List.of(Map.of(
                "role", "user",
                "content", List.of(Map.of(
                        "type", "input_audio",
                        "input_audio", Map.of("data", dataUrl)
                ))
        )));
        request.put("stream", false);
        request.put("asr_options", asrOptions);

        long start = System.currentTimeMillis();
        try {
            Map<?, ?> response = restClient.post()
                    .uri(properties.getBaseUrl() + "/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getApiKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(Map.class);
            String transcript = extractText(response);
            logCall(properties.getAudioModel(), audioBytes.length, transcript.length(),
                    System.currentTimeMillis() - start, true, null);
            return transcript;
        } catch (RuntimeException ex) {
            logCall(properties.getAudioModel(), audioBytes.length, 0,
                    System.currentTimeMillis() - start, false, ex.getMessage());
            throw ex;
        }
    }

    @SuppressWarnings("unchecked")
    private String extractText(Map<?, ?> response) {
        if (response == null) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Qwen ASR returned an empty response");
        }
        Object choicesObject = response.get("choices");
        if (choicesObject instanceof List<?> choices && !choices.isEmpty()) {
            Object firstChoice = choices.get(0);
            if (firstChoice instanceof Map<?, ?> choice) {
                Object messageObject = choice.get("message");
                if (messageObject instanceof Map<?, ?> message) {
                    String content = readContent(message.get("content"));
                    if (StringUtils.hasText(content)) {
                        return content.trim();
                    }
                }
            }
        }
        Object outputObject = response.get("output");
        if (outputObject instanceof Map<?, ?> output) {
            Object text = output.get("text");
            if (text instanceof String value && StringUtils.hasText(value)) {
                return value.trim();
            }
            Object outputChoicesObject = output.get("choices");
            if (outputChoicesObject instanceof List<?> outputChoices && !outputChoices.isEmpty()) {
                Object firstChoice = outputChoices.get(0);
                if (firstChoice instanceof Map<?, ?> choice) {
                    Object messageObject = choice.get("message");
                    if (messageObject instanceof Map<?, ?> message) {
                        String content = readContent(message.get("content"));
                        if (StringUtils.hasText(content)) {
                            return content.trim();
                        }
                    }
                }
            }
        }
        throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Qwen ASR returned an unexpected response format");
    }

    private String readContent(Object contentObject) {
        if (contentObject instanceof String text) {
            return text;
        }
        if (contentObject instanceof List<?> list) {
            StringBuilder builder = new StringBuilder();
            for (Object item : list) {
                if (item instanceof Map<?, ?> map) {
                    Object text = map.get("text");
                    if (text instanceof String value) {
                        builder.append(value);
                    }
                }
            }
            return builder.toString();
        }
        return null;
    }

    private String normalizeLanguage(String languageHint) {
        if (!StringUtils.hasText(languageHint)) {
            return "zh";
        }
        String value = languageHint.trim().toLowerCase();
        if (value.startsWith("zh")) {
            return "zh";
        }
        if (value.startsWith("en")) {
            return "en";
        }
        return value;
    }

    private void logCall(String model, int promptChars, int responseChars,
                         long elapsedMs, boolean success, String errorMessage) {
        try {
            AiCallLog log = new AiCallLog();
            log.setScenario("speech");
            log.setModel(model);
            log.setPromptChars(promptChars);
            log.setResponseChars(responseChars);
            log.setElapsedMs(elapsedMs);
            log.setSuccess(success ? 1 : 0);
            log.setErrorMessage(errorMessage == null ? null : errorMessage.substring(0, Math.min(500, errorMessage.length())));
            log.setCreatedAt(LocalDateTime.now());
            aiCallLogMapper.insert(log);
        } catch (RuntimeException ignored) {
            // AI call logging must not affect the main request.
        }
    }
}
