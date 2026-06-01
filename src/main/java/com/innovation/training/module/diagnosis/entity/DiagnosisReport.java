package com.innovation.training.module.diagnosis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("diagnosis_report")
public class DiagnosisReport {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("student_name")
    private String studentName;

    @TableField("class_name")
    private String className;

    private String subject;

    private String topic;

    @TableField("question_text")
    private String questionText;

    @TableField("answer_text")
    private String answerText;

    @TableField("image_url")
    private String imageUrl;

    @TableField("report_json")
    private String reportJson;

    @TableField("root_cause")
    private String rootCause;

    private String interventions;

    @TableField("created_at")
    private LocalDateTime createdAt;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public String getStudentName() { return studentName; }

    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getClassName() { return className; }

    public void setClassName(String className) { this.className = className; }

    public String getSubject() { return subject; }

    public void setSubject(String subject) { this.subject = subject; }

    public String getTopic() { return topic; }

    public void setTopic(String topic) { this.topic = topic; }

    public String getQuestionText() { return questionText; }

    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getAnswerText() { return answerText; }

    public void setAnswerText(String answerText) { this.answerText = answerText; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getReportJson() { return reportJson; }

    public void setReportJson(String reportJson) { this.reportJson = reportJson; }

    public String getRootCause() { return rootCause; }

    public void setRootCause(String rootCause) { this.rootCause = rootCause; }

    public String getInterventions() { return interventions; }

    public void setInterventions(String interventions) { this.interventions = interventions; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
