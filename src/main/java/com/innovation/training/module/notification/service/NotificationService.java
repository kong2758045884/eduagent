package com.innovation.training.module.notification.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.innovation.training.common.BusinessException;
import com.innovation.training.common.ErrorCode;
import com.innovation.training.module.notification.dto.NotificationResponse;
import com.innovation.training.module.notification.entity.AppNotification;
import com.innovation.training.module.notification.mapper.AppNotificationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final AppNotificationMapper mapper;

    public NotificationService(AppNotificationMapper mapper) {
        this.mapper = mapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public void notify(Long userId, String title, String content, String type, String sourceType, Long sourceId) {
        if (userId == null) {
            return;
        }
        AppNotification item = new AppNotification();
        item.setUserId(userId);
        item.setTitle(title);
        item.setContent(content);
        item.setType(type);
        item.setSourceType(sourceType);
        item.setSourceId(sourceId);
        item.setReadStatus(0);
        item.setCreatedAt(LocalDateTime.now());
        mapper.insert(item);
    }

    public List<NotificationResponse> list(Long userId, Boolean unreadOnly) {
        LambdaQueryWrapper<AppNotification> wrapper = new LambdaQueryWrapper<AppNotification>()
                .eq(AppNotification::getUserId, userId)
                .orderByDesc(AppNotification::getCreatedAt);
        if (Boolean.TRUE.equals(unreadOnly)) {
            wrapper.eq(AppNotification::getReadStatus, 0);
        }
        return mapper.selectList(wrapper).stream().map(NotificationResponse::from).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public NotificationResponse markRead(Long userId, Long id) {
        AppNotification item = mapper.selectOne(new LambdaQueryWrapper<AppNotification>()
                .eq(AppNotification::getId, id)
                .eq(AppNotification::getUserId, userId)
                .last("LIMIT 1"));
        if (item == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "通知不存在");
        }
        item.setReadStatus(1);
        mapper.updateById(item);
        return NotificationResponse.from(item);
    }
}
