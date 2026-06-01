package com.innovation.training.module.experience.dto;

import com.innovation.training.module.lesson.dto.LessonResponse;
import com.innovation.training.module.lesson.dto.ReflectionResponse;

import java.util.List;

public class ExperienceItemResponse {

    private LessonResponse lesson;
    private List<ReflectionResponse> reflections;

    public ExperienceItemResponse() {
    }

    public ExperienceItemResponse(LessonResponse lesson, List<ReflectionResponse> reflections) {
        this.lesson = lesson;
        this.reflections = reflections;
    }

    public LessonResponse getLesson() { return lesson; }
    public void setLesson(LessonResponse lesson) { this.lesson = lesson; }
    public List<ReflectionResponse> getReflections() { return reflections; }
    public void setReflections(List<ReflectionResponse> reflections) { this.reflections = reflections; }
}
