package com.innovation.training.module.ai.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class AvatarChatRequest {

    @NotBlank(message = "问题内容不能为空")
    @Size(max = 10000, message = "问题内容不能超过 10000 个字符")
    private String prompt;

    @NotBlank(message = "教学风格不能为空")
    @Size(max = 50, message = "教学风格不能超过 50 个字符")
    private String style;

    @Valid
    @Size(max = 40, message = "最多携带 40 条历史消息")
    private List<AvatarChatMessageRequest> history = new ArrayList<>();

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public List<AvatarChatMessageRequest> getHistory() {
        return history;
    }

    public void setHistory(List<AvatarChatMessageRequest> history) {
        this.history = history == null ? new ArrayList<>() : history;
    }
}
