package com.innovation.training.module.growth.dto;

import java.util.List;
import java.util.Map;

public class GrowthPortfolioResponse {

    private Map<String, Long> counters;
    private List<MonthStat> monthStats;
    private List<GrowthEventResponse> events;
    private List<GrowthFeedbackResponse> feedbacks;

    public Map<String, Long> getCounters() { return counters; }
    public void setCounters(Map<String, Long> counters) { this.counters = counters; }
    public List<MonthStat> getMonthStats() { return monthStats; }
    public void setMonthStats(List<MonthStat> monthStats) { this.monthStats = monthStats; }
    public List<GrowthEventResponse> getEvents() { return events; }
    public void setEvents(List<GrowthEventResponse> events) { this.events = events; }
    public List<GrowthFeedbackResponse> getFeedbacks() { return feedbacks; }
    public void setFeedbacks(List<GrowthFeedbackResponse> feedbacks) { this.feedbacks = feedbacks; }

    public static class MonthStat {
        private String month;
        private Long count;

        public MonthStat() {
        }

        public MonthStat(String month, Long count) {
            this.month = month;
            this.count = count;
        }

        public String getMonth() { return month; }
        public void setMonth(String month) { this.month = month; }
        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
    }
}
