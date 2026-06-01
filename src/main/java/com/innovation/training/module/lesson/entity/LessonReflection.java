package com.innovation.training.module.lesson.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("lesson_reflection")
public class LessonReflection {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("lesson_id")
    private Long lessonId;

    private String text;

    @TableField("ai_summary")
    private String aiSummary;

    private Integer shared;

    @TableField("created_at")
    private LocalDateTime createdAt;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public Long getLessonId() { return lessonId; }

    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    public String getAiSummary() { return aiSummary; }

    public void setAiSummary(String aiSummary) { this.aiSummary = aiSummary; }

    public Integer getShared() { return shared; }

    public void setShared(Integer shared) { this.shared = shared; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
