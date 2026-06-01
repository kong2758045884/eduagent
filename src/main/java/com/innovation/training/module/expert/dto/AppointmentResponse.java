package com.innovation.training.module.expert.dto;

import com.innovation.training.module.expert.entity.ExpertAppointment;

import java.time.LocalDateTime;

public class AppointmentResponse {

    private Long id;
    private Long expertId;
    private Long topicId;
    private String title;
    private String question;
    private LocalDateTime appointmentTime;
    private String status;
    private String meetingUrl;
    private String replyNote;
    private LocalDateTime createdAt;
    private ExpertResponse expert;

    public static AppointmentResponse from(ExpertAppointment appointment, ExpertResponse expert) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setExpertId(appointment.getExpertId());
        response.setTopicId(appointment.getTopicId());
        response.setTitle(appointment.getTitle());
        response.setQuestion(appointment.getQuestion());
        response.setAppointmentTime(appointment.getAppointmentTime());
        response.setStatus(appointment.getStatus());
        response.setMeetingUrl(appointment.getMeetingUrl());
        response.setReplyNote(appointment.getReplyNote());
        response.setCreatedAt(appointment.getCreatedAt());
        response.setExpert(expert);
        return response;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getExpertId() { return expertId; }
    public void setExpertId(Long expertId) { this.expertId = expertId; }
    public Long getTopicId() { return topicId; }
    public void setTopicId(Long topicId) { this.topicId = topicId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalDateTime appointmentTime) { this.appointmentTime = appointmentTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMeetingUrl() { return meetingUrl; }
    public void setMeetingUrl(String meetingUrl) { this.meetingUrl = meetingUrl; }
    public String getReplyNote() { return replyNote; }
    public void setReplyNote(String replyNote) { this.replyNote = replyNote; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public ExpertResponse getExpert() { return expert; }
    public void setExpert(ExpertResponse expert) { this.expert = expert; }
}
