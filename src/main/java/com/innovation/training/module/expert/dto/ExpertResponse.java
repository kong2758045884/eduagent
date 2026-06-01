package com.innovation.training.module.expert.dto;

import com.innovation.training.module.expert.entity.Expert;

public class ExpertResponse {

    private Long id;
    private String name;
    private String title;
    private String organization;
    private String field;
    private String tags;
    private String introduction;
    private String availableSlots;

    public static ExpertResponse from(Expert expert) {
        ExpertResponse response = new ExpertResponse();
        response.setId(expert.getId());
        response.setName(expert.getName());
        response.setTitle(expert.getTitle());
        response.setOrganization(expert.getOrganization());
        response.setField(expert.getField());
        response.setTags(expert.getTags());
        response.setIntroduction(expert.getIntroduction());
        response.setAvailableSlots(expert.getAvailableSlots());
        return response;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }
    public String getField() { return field; }
    public void setField(String field) { this.field = field; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public String getAvailableSlots() { return availableSlots; }
    public void setAvailableSlots(String availableSlots) { this.availableSlots = availableSlots; }
}
