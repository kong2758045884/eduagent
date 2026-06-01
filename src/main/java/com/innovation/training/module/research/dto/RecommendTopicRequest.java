package com.innovation.training.module.research.dto;

import java.util.List;

public class RecommendTopicRequest {

    private String teacherGoal;

    private List<String> sources;

    private Boolean save;

    public String getTeacherGoal() { return teacherGoal; }

    public void setTeacherGoal(String teacherGoal) { this.teacherGoal = teacherGoal; }

    public List<String> getSources() { return sources; }

    public void setSources(List<String> sources) { this.sources = sources; }

    public Boolean getSave() { return save; }

    public void setSave(Boolean save) { this.save = save; }
}
