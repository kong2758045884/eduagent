package com.innovation.training.module.research.service;

import com.innovation.training.module.research.dto.RecommendTopicRequest;
import com.innovation.training.module.research.dto.RecommendTopicResponse;
import com.innovation.training.module.research.dto.ResearchTopicResponse;
import com.innovation.training.module.research.dto.SaveTopicRequest;

import java.util.List;

public interface ResearchService {

    RecommendTopicResponse recommend(Long userId, RecommendTopicRequest request);

    ResearchTopicResponse save(Long userId, SaveTopicRequest request);

    List<ResearchTopicResponse> list(Long userId);

    ResearchTopicResponse update(Long userId, Long id, SaveTopicRequest request);

    void delete(Long userId, Long id);
}
