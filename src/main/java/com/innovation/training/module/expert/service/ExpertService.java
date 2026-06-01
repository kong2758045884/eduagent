package com.innovation.training.module.expert.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.innovation.training.common.BusinessException;
import com.innovation.training.common.ErrorCode;
import com.innovation.training.module.expert.dto.AppointmentResponse;
import com.innovation.training.module.expert.dto.CreateAppointmentRequest;
import com.innovation.training.module.expert.dto.ExpertResponse;
import com.innovation.training.module.expert.dto.UpdateAppointmentRequest;
import com.innovation.training.module.expert.entity.Expert;
import com.innovation.training.module.expert.entity.ExpertAppointment;
import com.innovation.training.module.expert.mapper.ExpertAppointmentMapper;
import com.innovation.training.module.expert.mapper.ExpertMapper;
import com.innovation.training.module.growth.service.GrowthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpertService {

    private final ExpertMapper expertMapper;
    private final ExpertAppointmentMapper appointmentMapper;
    private final GrowthService growthService;

    public ExpertService(ExpertMapper expertMapper,
                         ExpertAppointmentMapper appointmentMapper,
                         GrowthService growthService) {
        this.expertMapper = expertMapper;
        this.appointmentMapper = appointmentMapper;
        this.growthService = growthService;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<ExpertResponse> list(String field) {
        seedExpertsIfEmpty();
        LambdaQueryWrapper<Expert> wrapper = new LambdaQueryWrapper<Expert>()
                .eq(Expert::getStatus, 1)
                .orderByDesc(Expert::getUpdatedAt);
        if (StringUtils.hasText(field)) {
            wrapper.like(Expert::getField, field.trim());
        }
        return expertMapper.selectList(wrapper).stream().map(ExpertResponse::from).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public AppointmentResponse createAppointment(Long userId, CreateAppointmentRequest request) {
        Expert expert = expertMapper.selectById(request.getExpertId());
        if (expert == null || expert.getStatus() == null || expert.getStatus() == 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "专家不存在或不可预约");
        }
        ExpertAppointment appointment = new ExpertAppointment();
        appointment.setUserId(userId);
        appointment.setExpertId(expert.getId());
        appointment.setTopicId(request.getTopicId());
        appointment.setTitle(request.getTitle().trim());
        appointment.setQuestion(request.getQuestion().trim());
        appointment.setAppointmentTime(request.getAppointmentTime() == null
                ? LocalDateTime.now().plusDays(3)
                : request.getAppointmentTime());
        ensureSlotAvailable(expert.getId(), appointment.getAppointmentTime(), null);
        appointment.setStatus("pending");
        appointment.setMeetingUrl(buildMeetingUrl(expert.getId(), appointment.getAppointmentTime()));
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedAt(LocalDateTime.now());
        appointmentMapper.insert(appointment);
        growthService.record(userId, "expert_appointment_created", "预约专家咨询：" + appointment.getTitle(),
                appointment.getQuestion(), "expert_appointment", appointment.getId());
        return AppointmentResponse.from(appointment, ExpertResponse.from(expert));
    }

    public List<AppointmentResponse> listAppointments(Long userId) {
        return appointmentMapper.selectList(new LambdaQueryWrapper<ExpertAppointment>()
                        .eq(ExpertAppointment::getUserId, userId)
                        .orderByDesc(ExpertAppointment::getCreatedAt))
                .stream()
                .map(appointment -> AppointmentResponse.from(appointment, findExpert(appointment.getExpertId())))
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public AppointmentResponse updateAppointment(Long userId, Long id, UpdateAppointmentRequest request) {
        ExpertAppointment appointment = appointmentMapper.selectOne(new LambdaQueryWrapper<ExpertAppointment>()
                .eq(ExpertAppointment::getId, id)
                .eq(ExpertAppointment::getUserId, userId)
                .last("LIMIT 1"));
        if (appointment == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "预约不存在");
        }
        if (StringUtils.hasText(request.getStatus())) {
            String status = request.getStatus().trim();
            if (!List.of("pending", "confirmed", "cancelled", "completed").contains(status)) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "预约状态非法");
            }
            appointment.setStatus(status);
        }
        if (StringUtils.hasText(request.getReplyNote())) {
            appointment.setReplyNote(request.getReplyNote().trim());
        }
        if (StringUtils.hasText(request.getMeetingUrl())) {
            appointment.setMeetingUrl(request.getMeetingUrl().trim());
        }
        appointment.setUpdatedAt(LocalDateTime.now());
        appointmentMapper.updateById(appointment);
        growthService.record(userId, "expert_appointment_updated", "更新专家咨询：" + appointment.getTitle(),
                appointment.getStatus(), "expert_appointment", appointment.getId());
        return AppointmentResponse.from(appointment, findExpert(appointment.getExpertId()));
    }

    private void ensureSlotAvailable(Long expertId, LocalDateTime appointmentTime, Long excludeId) {
        if (appointmentTime == null) {
            return;
        }
        LambdaQueryWrapper<ExpertAppointment> wrapper = new LambdaQueryWrapper<ExpertAppointment>()
                .eq(ExpertAppointment::getExpertId, expertId)
                .eq(ExpertAppointment::getAppointmentTime, appointmentTime)
                .in(ExpertAppointment::getStatus, List.of("pending", "confirmed"));
        if (excludeId != null) {
            wrapper.ne(ExpertAppointment::getId, excludeId);
        }
        Long count = appointmentMapper.selectCount(wrapper);
        if (count != null && count > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "该专家此时间段已被预约");
        }
    }

    private String buildMeetingUrl(Long expertId, LocalDateTime appointmentTime) {
        return "https://meeting.example.local/expert-" + expertId + "-" + appointmentTime.toLocalDate();
    }

    private ExpertResponse findExpert(Long expertId) {
        Expert expert = expertMapper.selectById(expertId);
        return expert == null ? null : ExpertResponse.from(expert);
    }

    private void seedExpertsIfEmpty() {
        Long count = expertMapper.selectCount(null);
        if (count != null && count > 0) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        insertSeed("刘明华", "教授", "省师范大学数学教育研究中心",
                "数学课程与教学论", "课题设计,错题研究,乡村数学", "长期指导县域数学教研课题，擅长把错题数据转化为研究问题。", now);
        insertSeed("陈思远", "高级教师", "县教师发展中心",
                "课堂诊断与教学评价", "听评课,课堂观察,考核材料", "熟悉本地教育局考核材料要求，可指导职称申报佐证材料整理。", now);
        insertSeed("徐雅琴", "教研员", "市教育科学研究院",
                "教育技术与人机协同", "AI教学,数字资源,成果转化", "关注 AI 工具在乡村课堂中的稳定落地和成果转化。", now);
    }

    private void insertSeed(String name, String title, String organization, String field,
                            String tags, String introduction, LocalDateTime now) {
        Expert expert = new Expert();
        expert.setName(name);
        expert.setTitle(title);
        expert.setOrganization(organization);
        expert.setField(field);
        expert.setTags(tags);
        expert.setIntroduction(introduction);
        expert.setAvailableSlots("周二 19:00-20:00；周四 19:30-20:30");
        expert.setStatus(1);
        expert.setCreatedAt(now);
        expert.setUpdatedAt(now);
        expertMapper.insert(expert);
    }
}
