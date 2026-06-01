package com.innovation.training.module.growth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.innovation.training.module.growth.dto.CreateGrowthEventRequest;
import com.innovation.training.module.growth.dto.CreateGrowthFeedbackRequest;
import com.innovation.training.module.growth.dto.GrowthEventResponse;
import com.innovation.training.module.growth.dto.GrowthFeedbackResponse;
import com.innovation.training.module.growth.dto.GrowthPortfolioResponse;
import com.innovation.training.module.growth.entity.GrowthEvent;
import com.innovation.training.module.growth.entity.GrowthFeedback;
import com.innovation.training.module.growth.mapper.GrowthEventMapper;
import com.innovation.training.module.growth.mapper.GrowthFeedbackMapper;
import com.innovation.training.module.growth.service.GrowthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GrowthServiceImpl implements GrowthService {

    private final GrowthEventMapper growthEventMapper;
    private final GrowthFeedbackMapper growthFeedbackMapper;

    public GrowthServiceImpl(GrowthEventMapper growthEventMapper, GrowthFeedbackMapper growthFeedbackMapper) {
        this.growthEventMapper = growthEventMapper;
        this.growthFeedbackMapper = growthFeedbackMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GrowthEventResponse create(Long userId, CreateGrowthEventRequest request) {
        GrowthEvent event = new GrowthEvent();
        event.setUserId(userId);
        event.setEventType(defaultText(request.getEventType(), "manual"));
        event.setTitle(request.getTitle().trim());
        event.setContent(trimToNull(request.getContent()));
        event.setSourceType(trimToNull(request.getSourceType()));
        event.setSourceId(request.getSourceId());
        event.setEventTime(request.getEventTime() == null ? LocalDateTime.now() : request.getEventTime());
        event.setCreatedAt(LocalDateTime.now());
        growthEventMapper.insert(event);
        return GrowthEventResponse.from(event);
    }

    @Override
    public void record(Long userId, String eventType, String title, String content, String sourceType, Long sourceId) {
        if (userId == null || !StringUtils.hasText(title)) {
            return;
        }
        CreateGrowthEventRequest request = new CreateGrowthEventRequest();
        request.setEventType(eventType);
        request.setTitle(title);
        request.setContent(content);
        request.setSourceType(sourceType);
        request.setSourceId(sourceId);
        create(userId, request);
    }

    @Override
    public List<GrowthEventResponse> list(Long userId) {
        return growthEventMapper.selectList(new LambdaQueryWrapper<GrowthEvent>()
                        .eq(GrowthEvent::getUserId, userId)
                        .orderByDesc(GrowthEvent::getEventTime))
                .stream()
                .map(GrowthEventResponse::from)
                .toList();
    }

    @Override
    public GrowthPortfolioResponse portfolio(Long userId) {
        List<GrowthEvent> events = growthEventMapper.selectList(new LambdaQueryWrapper<GrowthEvent>()
                .eq(GrowthEvent::getUserId, userId)
                .orderByDesc(GrowthEvent::getEventTime));
        List<GrowthFeedbackResponse> feedbacks = listFeedback(userId, null);
        Map<String, Long> counters = events.stream()
                .collect(Collectors.groupingBy(GrowthEvent::getEventType, LinkedHashMap::new, Collectors.counting()));
        counters.put("total", (long) events.size());
        counters.put("feedbacks", (long) feedbacks.size());

        Map<String, Long> monthCounts = events.stream()
                .filter(event -> event.getEventTime() != null)
                .collect(Collectors.groupingBy(event -> YearMonth.from(event.getEventTime()).toString(),
                        LinkedHashMap::new, Collectors.counting()));
        List<GrowthPortfolioResponse.MonthStat> monthStats = monthCounts.entrySet()
                .stream()
                .map(entry -> new GrowthPortfolioResponse.MonthStat(entry.getKey(), entry.getValue()))
                .toList();

        GrowthPortfolioResponse response = new GrowthPortfolioResponse();
        response.setCounters(counters);
        response.setMonthStats(monthStats);
        response.setEvents(events.stream().map(GrowthEventResponse::from).toList());
        response.setFeedbacks(feedbacks);
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GrowthFeedbackResponse createFeedback(Long userId, CreateGrowthFeedbackRequest request) {
        GrowthFeedback feedback = new GrowthFeedback();
        feedback.setUserId(userId);
        feedback.setFeedbackType(defaultText(request.getFeedbackType(), "mentor_comment"));
        feedback.setSource(trimToNull(request.getSource()));
        feedback.setTitle(request.getTitle().trim());
        feedback.setContent(request.getContent().trim());
        feedback.setSourceType(trimToNull(request.getSourceType()));
        feedback.setSourceId(request.getSourceId());
        feedback.setCreatedAt(LocalDateTime.now());
        growthFeedbackMapper.insert(feedback);
        record(userId, "growth_feedback_" + feedback.getFeedbackType(), feedback.getTitle(),
                feedback.getContent(), "growth_feedback", feedback.getId());
        return GrowthFeedbackResponse.from(feedback);
    }

    @Override
    public List<GrowthFeedbackResponse> listFeedback(Long userId, String feedbackType) {
        LambdaQueryWrapper<GrowthFeedback> wrapper = new LambdaQueryWrapper<GrowthFeedback>()
                .eq(GrowthFeedback::getUserId, userId)
                .orderByDesc(GrowthFeedback::getCreatedAt);
        if (StringUtils.hasText(feedbackType)) {
            wrapper.eq(GrowthFeedback::getFeedbackType, feedbackType.trim());
        }
        return growthFeedbackMapper.selectList(wrapper).stream().map(GrowthFeedbackResponse::from).toList();
    }

    private String trimToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private String defaultText(String value, String fallback) {
        return StringUtils.hasText(value) ? value.trim() : fallback;
    }
}
