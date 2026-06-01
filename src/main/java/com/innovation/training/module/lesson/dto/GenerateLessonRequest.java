package com.innovation.training.module.lesson.dto;

import jakarta.validation.constraints.NotBlank;

public class GenerateLessonRequest {

    @NotBlank(message = "备课需求不能为空")
    private String requirement;

    private Boolean save;

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public Boolean getSave() {
        return save;
    }

    public void setSave(Boolean save) {
        this.save = save;
    }
}
