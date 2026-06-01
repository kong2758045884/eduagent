package com.innovation.training.module.ai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("ai_call_log")
public class AiCallLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String scenario;

    private String model;

    @TableField("prompt_chars")
    private Integer promptChars;

    @TableField("response_chars")
    private Integer responseChars;

    @TableField("elapsed_ms")
    private Long elapsedMs;

    private Integer success;

    @TableField("error_message")
    private String errorMessage;

    @TableField("created_at")
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getScenario() { return scenario; }
    public void setScenario(String scenario) { this.scenario = scenario; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public Integer getPromptChars() { return promptChars; }
    public void setPromptChars(Integer promptChars) { this.promptChars = promptChars; }
    public Integer getResponseChars() { return responseChars; }
    public void setResponseChars(Integer responseChars) { this.responseChars = responseChars; }
    public Long getElapsedMs() { return elapsedMs; }
    public void setElapsedMs(Long elapsedMs) { this.elapsedMs = elapsedMs; }
    public Integer getSuccess() { return success; }
    public void setSuccess(Integer success) { this.success = success; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
