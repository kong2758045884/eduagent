package com.innovation.training.module.report.dto;

import java.time.LocalDate;
import java.util.List;

public class ReportRequest {

    private LocalDate startDate;

    private LocalDate endDate;

    private List<Long> lessonIds;

    private List<Long> reflectionIds;

    private String title;

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public List<Long> getLessonIds() { return lessonIds; }
    public void setLessonIds(List<Long> lessonIds) { this.lessonIds = lessonIds; }
    public List<Long> getReflectionIds() { return reflectionIds; }
    public void setReflectionIds(List<Long> reflectionIds) { this.reflectionIds = reflectionIds; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
