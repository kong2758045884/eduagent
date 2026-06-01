package com.innovation.training.module.research.dto;

public class RecommendTopicResponse {

    private String recommendationJson;
    private ResearchTopicResponse savedTopic;

    public RecommendTopicResponse() {
    }

    public RecommendTopicResponse(String recommendationJson, ResearchTopicResponse savedTopic) {
        this.recommendationJson = recommendationJson;
        this.savedTopic = savedTopic;
    }

    public String getRecommendationJson() { return recommendationJson; }

    public void setRecommendationJson(String recommendationJson) { this.recommendationJson = recommendationJson; }

    public ResearchTopicResponse getSavedTopic() { return savedTopic; }

    public void setSavedTopic(ResearchTopicResponse savedTopic) { this.savedTopic = savedTopic; }
}
