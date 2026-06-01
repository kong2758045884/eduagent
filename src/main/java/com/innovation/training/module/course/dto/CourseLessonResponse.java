package com.innovation.training.module.course.dto;

import com.innovation.training.module.course.entity.TrainingCourseLesson;

public class CourseLessonResponse {
    private Long id;
    private String title;
    private String content;
    private String videoUrl;
    private Integer duration;
    private Integer sortOrder;

    public static CourseLessonResponse from(TrainingCourseLesson lesson) {
        CourseLessonResponse response = new CourseLessonResponse();
        response.setId(lesson.getId());
        response.setTitle(lesson.getTitle());
        response.setContent(lesson.getContent());
        response.setVideoUrl(lesson.getVideoUrl());
        response.setDuration(lesson.getDuration());
        response.setSortOrder(lesson.getSortOrder());
        return response;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
