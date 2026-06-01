package com.innovation.training.module.diagnosis.service;

import java.util.List;

public class ClassHeatmapResponse {

    private String className;
    private Long totalReports;
    private Long highRiskStudents;
    private List<HeatmapTopicItem> topics;
    private List<StudentDiagnosisSummaryResponse> students;

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public Long getTotalReports() { return totalReports; }
    public void setTotalReports(Long totalReports) { this.totalReports = totalReports; }
    public Long getHighRiskStudents() { return highRiskStudents; }
    public void setHighRiskStudents(Long highRiskStudents) { this.highRiskStudents = highRiskStudents; }
    public List<HeatmapTopicItem> getTopics() { return topics; }
    public void setTopics(List<HeatmapTopicItem> topics) { this.topics = topics; }
    public List<StudentDiagnosisSummaryResponse> getStudents() { return students; }
    public void setStudents(List<StudentDiagnosisSummaryResponse> students) { this.students = students; }

    public static class HeatmapTopicItem {
        private String topic;
        private Long count;
        private String level;

        public HeatmapTopicItem() {
        }

        public HeatmapTopicItem(String topic, Long count, String level) {
            this.topic = topic;
            this.count = count;
            this.level = level;
        }

        public String getTopic() { return topic; }
        public void setTopic(String topic) { this.topic = topic; }
        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }
    }
}
