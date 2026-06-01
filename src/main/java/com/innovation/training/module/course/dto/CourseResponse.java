package com.innovation.training.module.course.dto;

import com.innovation.training.module.course.entity.TrainingCourse;

import java.util.List;

public class CourseResponse {
    private Long id;
    private String title;
    private String audience;
    private String category;
    private String summary;
    private String coverUrl;
    private Integer hours;
    private Integer progress;
    private String enrollmentStatus;
    private List<CourseLessonResponse> lessons;

    public static CourseResponse from(TrainingCourse course, Integer progress, String status, List<CourseLessonResponse> lessons) {
        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setTitle(course.getTitle());
        response.setAudience(course.getAudience());
        response.setCategory(course.getCategory());
        response.setSummary(course.getSummary());
        response.setCoverUrl(course.getCoverUrl());
        response.setHours(course.getHours());
        response.setProgress(progress);
        response.setEnrollmentStatus(status);
        response.setLessons(lessons);
        return response;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAudience() { return audience; }
    public void setAudience(String audience) { this.audience = audience; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
    public Integer getHours() { return hours; }
    public void setHours(Integer hours) { this.hours = hours; }
    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { this.progress = progress; }
    public String getEnrollmentStatus() { return enrollmentStatus; }
    public void setEnrollmentStatus(String enrollmentStatus) { this.enrollmentStatus = enrollmentStatus; }
    public List<CourseLessonResponse> getLessons() { return lessons; }
    public void setLessons(List<CourseLessonResponse> lessons) { this.lessons = lessons; }
}
