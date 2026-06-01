package com.innovation.training.module.qa.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.innovation.training.common.BusinessException;
import com.innovation.training.common.ErrorCode;
import com.innovation.training.module.growth.service.GrowthService;
import com.innovation.training.module.qa.dto.CreateQuestionRequest;
import com.innovation.training.module.qa.dto.CreateReplyRequest;
import com.innovation.training.module.qa.dto.QaQuestionResponse;
import com.innovation.training.module.qa.dto.QaReplyResponse;
import com.innovation.training.module.qa.entity.QaQuestion;
import com.innovation.training.module.qa.entity.QaReply;
import com.innovation.training.module.qa.mapper.QaQuestionMapper;
import com.innovation.training.module.qa.mapper.QaReplyMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QaService {

    private final QaQuestionMapper questionMapper;
    private final QaReplyMapper replyMapper;
    private final GrowthService growthService;

    public QaService(QaQuestionMapper questionMapper, QaReplyMapper replyMapper, GrowthService growthService) {
        this.questionMapper = questionMapper;
        this.replyMapper = replyMapper;
        this.growthService = growthService;
    }

    @Transactional(rollbackFor = Exception.class)
    public QaQuestionResponse create(Long userId, CreateQuestionRequest request) {
        QaQuestion question = new QaQuestion();
        question.setUserId(userId);
        question.setTopic(defaultText(request.getTopic(), "课堂问题"));
        question.setContent(request.getContent().trim());
        question.setStatus(request.getMentorUserId() == null ? "open" : "forwarded");
        question.setMentorUserId(request.getMentorUserId());
        question.setSourceType(trimToNull(request.getSourceType()));
        question.setSourceId(request.getSourceId());
        question.setCreatedAt(LocalDateTime.now());
        question.setUpdatedAt(LocalDateTime.now());
        questionMapper.insert(question);
        addSystemReply(question.getId(), "平台助理", "已收到问题，建议先补充学段、班级特点和已尝试方法。");
        growthService.record(userId, "qa_question_created", "发布课堂答疑：" + question.getTopic(),
                question.getContent(), "qa_question", question.getId());
        return toResponse(question);
    }

    public List<QaQuestionResponse> list(Long userId, Boolean mineOnly) {
        LambdaQueryWrapper<QaQuestion> wrapper = new LambdaQueryWrapper<QaQuestion>()
                .orderByDesc(QaQuestion::getCreatedAt);
        if (Boolean.TRUE.equals(mineOnly)) {
            wrapper.eq(QaQuestion::getUserId, userId);
        }
        return questionMapper.selectList(wrapper).stream().map(this::toResponse).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public QaReplyResponse reply(Long userId, Long questionId, CreateReplyRequest request) {
        QaQuestion question = requireQuestion(questionId);
        QaReply reply = new QaReply();
        reply.setQuestionId(questionId);
        reply.setUserId(userId);
        reply.setRole(defaultText(request.getRole(), "teacher"));
        reply.setContent(request.getContent().trim());
        reply.setCreatedAt(LocalDateTime.now());
        replyMapper.insert(reply);
        question.setStatus("answered");
        question.setUpdatedAt(LocalDateTime.now());
        questionMapper.updateById(question);
        growthService.record(userId, "qa_reply_created", "回复课堂答疑",
                reply.getContent(), "qa_question", questionId);
        return QaReplyResponse.from(reply);
    }

    @Transactional(rollbackFor = Exception.class)
    public QaQuestionResponse forward(Long userId, Long questionId, Long mentorUserId) {
        QaQuestion question = requireQuestion(questionId);
        if (!question.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "只能转发自己的问题");
        }
        question.setMentorUserId(mentorUserId);
        question.setStatus("forwarded");
        question.setUpdatedAt(LocalDateTime.now());
        questionMapper.updateById(question);
        growthService.record(userId, "qa_question_forwarded", "转发问题给名师",
                question.getContent(), "qa_question", question.getId());
        return toResponse(question);
    }

    private QaQuestion requireQuestion(Long questionId) {
        QaQuestion question = questionMapper.selectById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "问题不存在");
        }
        return question;
    }

    private QaQuestionResponse toResponse(QaQuestion question) {
        List<QaReplyResponse> replies = replyMapper.selectList(new LambdaQueryWrapper<QaReply>()
                        .eq(QaReply::getQuestionId, question.getId())
                        .orderByAsc(QaReply::getCreatedAt))
                .stream()
                .map(QaReplyResponse::from)
                .toList();
        return QaQuestionResponse.from(question, replies);
    }

    private void addSystemReply(Long questionId, String role, String content) {
        QaReply reply = new QaReply();
        reply.setQuestionId(questionId);
        reply.setUserId(0L);
        reply.setRole(role);
        reply.setContent(content);
        reply.setCreatedAt(LocalDateTime.now());
        replyMapper.insert(reply);
    }

    private String trimToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private String defaultText(String value, String fallback) {
        return StringUtils.hasText(value) ? value.trim() : fallback;
    }
}
