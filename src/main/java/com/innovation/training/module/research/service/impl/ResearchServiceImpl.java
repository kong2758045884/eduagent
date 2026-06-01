package com.innovation.training.module.research.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovation.training.module.ai.AiGenerationService;
import com.innovation.training.module.research.dto.RecommendTopicRequest;
import com.innovation.training.module.research.dto.RecommendTopicResponse;
import com.innovation.training.module.research.dto.ResearchTopicResponse;
import com.innovation.training.module.research.dto.SaveTopicRequest;
import com.innovation.training.module.research.entity.ResearchTopic;
import com.innovation.training.module.research.mapper.ResearchTopicMapper;
import com.innovation.training.module.research.service.ResearchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResearchServiceImpl implements ResearchService {

    private final ResearchTopicMapper researchTopicMapper;
    private final AiGenerationService aiGenerationService;
    private final ObjectMapper objectMapper;

    public ResearchServiceImpl(ResearchTopicMapper researchTopicMapper,
                               AiGenerationService aiGenerationService,
                               ObjectMapper objectMapper) {
        this.researchTopicMapper = researchTopicMapper;
        this.aiGenerationService = aiGenerationService;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RecommendTopicResponse recommend(Long userId, RecommendTopicRequest request) {
        String json = stripCodeFence(aiGenerationService.recommendResearchTopic(request.getSources(), request.getTeacherGoal()));
        ResearchTopicResponse saved = null;
        if (Boolean.TRUE.equals(request.getSave())) {
            SaveTopicRequest saveRequest = new SaveTopicRequest();
            saveRequest.setTitle(readText(json, "title", "AI 推荐课题"));
            saveRequest.setMeta(readText(json, "value", null));
            saveRequest.setExtra(readText(json, "plan", null));
            saveRequest.setSources(request.getSources() == null ? null : String.join("\n", request.getSources()));
            saveRequest.setApplicationDraft(readText(json, "applicationDraft", json));
            saved = save(userId, saveRequest);
        }
        return new RecommendTopicResponse(json, saved);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResearchTopicResponse save(Long userId, SaveTopicRequest request) {
        ResearchTopic topic = new ResearchTopic();
        topic.setUserId(userId);
        topic.setTitle(request.getTitle().trim());
        topic.setMeta(trimToNull(request.getMeta()));
        topic.setExtra(trimToNull(request.getExtra()));
        topic.setSources(trimToNull(request.getSources()));
        topic.setApplicationDraft(trimToNull(request.getApplicationDraft()));
        LocalDateTime now = LocalDateTime.now();
        topic.setCreatedAt(now);
        topic.setUpdatedAt(now);
        researchTopicMapper.insert(topic);
        return ResearchTopicResponse.from(topic);
    }

    @Override
    public List<ResearchTopicResponse> list(Long userId) {
        return researchTopicMapper.selectList(new LambdaQueryWrapper<ResearchTopic>()
                        .eq(ResearchTopic::getUserId, userId)
                        .orderByDesc(ResearchTopic::getUpdatedAt))
                .stream()
                .map(ResearchTopicResponse::from)
                .toList();
    }

    private String readText(String json, String field, String fallback) {
        try {
            JsonNode node = objectMapper.readTree(json).path(field);
            if (node.isArray()) {
                StringBuilder builder = new StringBuilder();
                for (JsonNode item : node) {
                    builder.append("- ").append(item.asText()).append("\n");
                }
                return builder.toString();
            }
            String value = node.asText();
            return StringUtils.hasText(value) ? value : fallback;
        } catch (Exception ex) {
            return fallback;
        }
    }

    private String trimToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private String stripCodeFence(String value) {
        String text = value == null ? "" : value.trim();
        if (text.startsWith("```")) {
            text = text.replaceFirst("^```(?:json)?", "").replaceFirst("```$", "").trim();
        }
        return text;
    }
}
