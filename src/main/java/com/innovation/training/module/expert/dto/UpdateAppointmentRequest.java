package com.innovation.training.module.expert.dto;

public class UpdateAppointmentRequest {

    private String status;
    private String replyNote;
    private String meetingUrl;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReplyNote() { return replyNote; }
    public void setReplyNote(String replyNote) { this.replyNote = replyNote; }
    public String getMeetingUrl() { return meetingUrl; }
    public void setMeetingUrl(String meetingUrl) { this.meetingUrl = meetingUrl; }
}
