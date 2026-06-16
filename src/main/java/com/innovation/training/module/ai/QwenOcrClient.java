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
import java.util.List;
import java.util.Map;

@Component
public class QwenOcrClient {

    private final QwenProperties properties;
    private final RestClient restClient;
    private final AiCallLogMapper aiCallLogMapper;

    public QwenOcrClient(QwenProperties properties, AiCallLogMapper aiCallLogMapper) {
        this.properties = properties;
        this.aiCallLogMapper = aiCallLogMapper;
        this.restClient = RestClient.builder()
                .requestFactory(ClientHttpRequestFactories.get(ClientHttpRequestFactorySettings.DEFAULTS
                        .withConnectTimeout(Duration.ofSeconds(properties.getTimeoutSeconds()))
                        .withReadTimeout(Duration.ofSeconds(properties.getTimeoutSeconds()))))
                .build();
    }

    public String recognizeWorksheet(byte[] imageBytes, String contentType) {
        if (!StringUtils.hasText(properties.getApiKey())) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "未配置 DASHSCOPE_API_KEY，无法调用千问 OCR");
        }
        if (imageBytes == null || imageBytes.length == 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "图片内容不能为空");
        }
        String mimeType = StringUtils.hasText(contentType) ? contentType : "image/jpeg";
        String dataUrl = "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(imageBytes);
        long start = System.currentTimeMillis();
        Map<String, Object> request = Map.of(
                "model", properties.getOcrModel(),
                "messages", List.of(
                        Map.of("role", "user", "content", List.of(
                                Map.of("type", "image_url", "image_url", Map.of("url", dataUrl)),
                                Map.of("type", "text", "text", """
                                        请识别这张学生错题/作业图片中的内容。
                                        重点提取：题目原文、学生手写答案、批改痕迹、关键算式。
                                        按纯文本输出，不要添加无关说明。
                                        """)
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
            String text = extractContent(response);
            logCall(imageBytes.length, text.length(), System.currentTimeMillis() - start, true, null);
            return text;
        } catch (RuntimeException ex) {
            logCall(imageBytes.length, 0, System.currentTimeMillis() - start, false, ex.getMessage());
            throw ex;
        }
    }

    private String extractContent(Map<?, ?> response) {
        if (response == null) throw new BusinessException(ErrorCode.INTERNAL_ERROR, "千问 OCR 返回为空");
        try {
            Object choicesObject = response.get("choices");
            if (choicesObject instanceof List<?> choices && !choices.isEmpty()) {
                Object firstChoice = choices.get(0);
                if (firstChoice instanceof Map<?, ?> choice) {
                    Object msgObj = choice.get("message");
                    if (msgObj instanceof Map<?, ?> message) {
                        Object content = message.get("content");
                        if (content instanceof String text) {
                if (StringUtils.hasText(text)) return text.trim();
                return "（图片中未识别到文字内容）";
            }
                        if (content instanceof List<?> parts) {
                            StringBuilder sb = new StringBuilder();
                            for (Object part : parts) {
                                if (part instanceof Map<?, ?> p) {
                                    Object t = p.get("text");
                                    if (t instanceof String s) sb.append(s);
                                    else if (t != null) sb.append(t.toString());
                                }
                            }
                            String result = sb.toString().trim();
                            if (!result.isEmpty()) return result;
                        }
                    }
                }
            }
            /* 如果以上都失败，尝试从 response 中提取任何文本 */
            String fallback = response.toString();
            if (fallback.length() > 2000) fallback = fallback.substring(0, 2000);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "OCR 解析失败，响应: " + fallback);
        } catch (BusinessException e) { throw e; }
        catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "OCR 解析异常: " + e.getMessage());
        }
    }

    private void logCall(int promptChars, int responseChars, long elapsedMs, boolean success, String errorMessage) {
        try {
            AiCallLog log = new AiCallLog();
            log.setScenario("ocr");
            log.setModel(properties.getOcrModel());
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
}
