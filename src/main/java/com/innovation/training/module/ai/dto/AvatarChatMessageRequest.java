package com.innovation.training.module.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AvatarChatMessageRequest {

    @NotBlank(message = "消息角色不能为空")
    @Pattern(regexp = "user|ai|assistant", message = "消息角色不合法")
    private String role;

    @NotBlank(message = "历史消息内容不能为空")
    @Size(max = 10000, message = "历史消息内容不能超过 10000 个字符")
    private String content;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
