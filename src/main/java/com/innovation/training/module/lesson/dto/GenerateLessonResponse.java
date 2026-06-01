package com.innovation.training.module.lesson.dto;

public class GenerateLessonResponse {

    private String contentJson;
    private String markdown;
    private LessonResponse savedLesson;

    public GenerateLessonResponse() {
    }

    public GenerateLessonResponse(String contentJson, String markdown, LessonResponse savedLesson) {
        this.contentJson = contentJson;
        this.markdown = markdown;
        this.savedLesson = savedLesson;
    }

    public String getContentJson() { return contentJson; }

    public void setContentJson(String contentJson) { this.contentJson = contentJson; }

    public String getMarkdown() { return markdown; }

    public void setMarkdown(String markdown) { this.markdown = markdown; }

    public LessonResponse getSavedLesson() { return savedLesson; }

    public void setSavedLesson(LessonResponse savedLesson) { this.savedLesson = savedLesson; }
}
