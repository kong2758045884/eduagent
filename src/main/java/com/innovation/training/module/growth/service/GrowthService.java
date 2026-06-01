package com.innovation.training.module.growth.service;

import com.innovation.training.module.growth.dto.CreateGrowthEventRequest;
import com.innovation.training.module.growth.dto.CreateGrowthFeedbackRequest;
import com.innovation.training.module.growth.dto.GrowthEventResponse;
import com.innovation.training.module.growth.dto.GrowthFeedbackResponse;
import com.innovation.training.module.growth.dto.GrowthPortfolioResponse;

import java.util.List;

public interface GrowthService {

    GrowthEventResponse create(Long userId, CreateGrowthEventRequest request);

    void record(Long userId, String eventType, String title, String content, String sourceType, Long sourceId);

    List<GrowthEventResponse> list(Long userId);

    GrowthPortfolioResponse portfolio(Long userId);

    GrowthFeedbackResponse createFeedback(Long userId, CreateGrowthFeedbackRequest request);

    List<GrowthFeedbackResponse> listFeedback(Long userId, String feedbackType);
}
