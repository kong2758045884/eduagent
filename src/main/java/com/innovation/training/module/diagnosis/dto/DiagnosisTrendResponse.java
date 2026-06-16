package com.innovation.training.module.diagnosis.dto;

import java.util.List;

public class DiagnosisTrendResponse {

    private List<String> days;
    private List<Long> counts;

    public DiagnosisTrendResponse(List<String> days, List<Long> counts) {
        this.days = days;
        this.counts = counts;
    }

    public List<String> getDays() { return days; }
    public void setDays(List<String> days) { this.days = days; }
    public List<Long> getCounts() { return counts; }
    public void setCounts(List<Long> counts) { this.counts = counts; }
}
