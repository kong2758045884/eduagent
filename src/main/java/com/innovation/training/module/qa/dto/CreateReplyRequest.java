package com.innovation.training.module.qa.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateReplyRequest {

    private String role = "teacher";

    @NotBlank(message = "回复内容不能为空")
    private String content;

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
