package com.innovation.training.module.lesson.service;

import com.innovation.training.module.lesson.dto.CreateReflectionRequest;
import com.innovation.training.module.lesson.dto.GenerateLessonRequest;
import com.innovation.training.module.lesson.dto.GenerateLessonResponse;
import com.innovation.training.module.lesson.dto.LessonResponse;
import com.innovation.training.module.lesson.dto.ReflectionResponse;
import com.innovation.training.module.lesson.dto.SaveLessonRequest;

import java.util.List;

public interface LessonService {

    GenerateLessonResponse generate(Long userId, GenerateLessonRequest request);

    LessonResponse saveLesson(Long userId, SaveLessonRequest request);

    LessonResponse updateLesson(Long userId, Long id, SaveLessonRequest request);

    List<LessonResponse> listLessons(Long userId);

    ReflectionResponse createReflection(Long userId, CreateReflectionRequest request);

    List<ReflectionResponse> listReflections(Long userId, Long lessonId);
}
