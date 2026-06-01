package com.innovation.training.module.ai;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ai.qwen")
public class QwenProperties {

    private String apiKey;

    private String baseUrl = "https://dashscope.aliyuncs.com/compatible-mode/v1";

    private String nativeBaseUrl = "https://dashscope.aliyuncs.com/api/v1";

    private String textModel = "qwen-plus";

    private String visionModel = "qwen-vl-plus";

    private String audioModel = "qwen3-asr-flash";

    private String ocrModel = "qwen-vl-ocr-latest";

    private double temperature = 0.4;

    private int timeoutSeconds = 60;

    private int speechMaxBase64Bytes = 10 * 1024 * 1024;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getNativeBaseUrl() {
        return nativeBaseUrl;
    }

    public void setNativeBaseUrl(String nativeBaseUrl) {
        this.nativeBaseUrl = nativeBaseUrl;
    }

    public String getTextModel() {
        return textModel;
    }

    public void setTextModel(String textModel) {
        this.textModel = textModel;
    }

    public String getVisionModel() {
        return visionModel;
    }

    public void setVisionModel(String visionModel) {
        this.visionModel = visionModel;
    }

    public String getAudioModel() {
        return audioModel;
    }

    public void setAudioModel(String audioModel) {
        this.audioModel = audioModel;
    }

    public String getOcrModel() {
        return ocrModel;
    }

    public void setOcrModel(String ocrModel) {
        this.ocrModel = ocrModel;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public int getSpeechMaxBase64Bytes() {
        return speechMaxBase64Bytes;
    }

    public void setSpeechMaxBase64Bytes(int speechMaxBase64Bytes) {
        this.speechMaxBase64Bytes = speechMaxBase64Bytes;
    }
}
