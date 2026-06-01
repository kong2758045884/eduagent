package com.innovation.training.module.course.dto;

public class UpdateCourseProgressRequest {
    private Integer progress;
    private Integer rating;
    private String feedback;

    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { this.progress = progress; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}
