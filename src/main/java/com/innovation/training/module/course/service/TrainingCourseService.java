package com.innovation.training.module.course.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.innovation.training.common.BusinessException;
import com.innovation.training.common.ErrorCode;
import com.innovation.training.module.course.dto.CourseLessonResponse;
import com.innovation.training.module.course.dto.CourseResponse;
import com.innovation.training.module.course.dto.UpdateCourseProgressRequest;
import com.innovation.training.module.course.entity.TrainingCourse;
import com.innovation.training.module.course.entity.TrainingCourseEnrollment;
import com.innovation.training.module.course.entity.TrainingCourseLesson;
import com.innovation.training.module.course.mapper.TrainingCourseEnrollmentMapper;
import com.innovation.training.module.course.mapper.TrainingCourseLessonMapper;
import com.innovation.training.module.course.mapper.TrainingCourseMapper;
import com.innovation.training.module.growth.service.GrowthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrainingCourseService {

    private final TrainingCourseMapper courseMapper;
    private final TrainingCourseLessonMapper lessonMapper;
    private final TrainingCourseEnrollmentMapper enrollmentMapper;
    private final GrowthService growthService;

    public TrainingCourseService(TrainingCourseMapper courseMapper,
                                 TrainingCourseLessonMapper lessonMapper,
                                 TrainingCourseEnrollmentMapper enrollmentMapper,
                                 GrowthService growthService) {
        this.courseMapper = courseMapper;
        this.lessonMapper = lessonMapper;
        this.enrollmentMapper = enrollmentMapper;
        this.growthService = growthService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void seedIfEmpty() {
        Long count = courseMapper.selectCount(null);
        if (count != null && count > 0) {
            return;
        }
        createSeed("AI教学入门速成课", "senior", "AI基础", "语音备课、随堂反思录制和一键导出经验册。", 8);
        createSeed("AI精准教学与科研提升课", "mid", "科研提效", "错题数据解读、课题导航和考核材料整理。", 16);
        createSeed("乡村教学场景AI适配课", "novice", "成长陪跑", "本土教法库、在线答疑和成长档案袋实操。", 12);
        createSeed("乡村AI教学工具实操与应用", "student", "师范生课程", "麦克风、拍摄仪、备课和诊断功能实训。", 24);
    }

    public List<CourseResponse> list(Long userId, String audience) {
        seedIfEmpty();
        LambdaQueryWrapper<TrainingCourse> wrapper = new LambdaQueryWrapper<TrainingCourse>()
                .eq(TrainingCourse::getStatus, 1)
                .orderByDesc(TrainingCourse::getUpdatedAt);
        if (StringUtils.hasText(audience)) {
            wrapper.eq(TrainingCourse::getAudience, audience.trim());
        }
        return courseMapper.selectList(wrapper).stream().map(course -> toResponse(userId, course)).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public CourseResponse enroll(Long userId, Long courseId) {
        TrainingCourse course = requireCourse(courseId);
        TrainingCourseEnrollment enrollment = enrollmentMapper.selectOne(new LambdaQueryWrapper<TrainingCourseEnrollment>()
                .eq(TrainingCourseEnrollment::getUserId, userId)
                .eq(TrainingCourseEnrollment::getCourseId, courseId)
                .last("LIMIT 1"));
        if (enrollment == null) {
            enrollment = new TrainingCourseEnrollment();
            enrollment.setUserId(userId);
            enrollment.setCourseId(courseId);
            enrollment.setProgress(0);
            enrollment.setStatus("learning");
            enrollment.setEnrolledAt(LocalDateTime.now());
            enrollment.setUpdatedAt(LocalDateTime.now());
            enrollmentMapper.insert(enrollment);
            growthService.record(userId, "course_enrolled", "报名课程：" + course.getTitle(),
                    course.getSummary(), "course", courseId);
        }
        return toResponse(userId, course);
    }

    @Transactional(rollbackFor = Exception.class)
    public CourseResponse updateProgress(Long userId, Long courseId, UpdateCourseProgressRequest request) {
        TrainingCourse course = requireCourse(courseId);
        TrainingCourseEnrollment enrollment = enrollmentMapper.selectOne(new LambdaQueryWrapper<TrainingCourseEnrollment>()
                .eq(TrainingCourseEnrollment::getUserId, userId)
                .eq(TrainingCourseEnrollment::getCourseId, courseId)
                .last("LIMIT 1"));
        if (enrollment == null) {
            enroll(userId, courseId);
            enrollment = enrollmentMapper.selectOne(new LambdaQueryWrapper<TrainingCourseEnrollment>()
                    .eq(TrainingCourseEnrollment::getUserId, userId)
                    .eq(TrainingCourseEnrollment::getCourseId, courseId)
                    .last("LIMIT 1"));
        }
        if (request.getProgress() != null) {
            int progress = Math.max(0, Math.min(100, request.getProgress()));
            enrollment.setProgress(progress);
            enrollment.setStatus(progress >= 100 ? "completed" : "learning");
        }
        enrollment.setRating(request.getRating());
        enrollment.setFeedback(trimToNull(request.getFeedback()));
        enrollment.setUpdatedAt(LocalDateTime.now());
        enrollmentMapper.updateById(enrollment);
        growthService.record(userId, "course_progress_updated", "更新课程进度：" + course.getTitle(),
                "进度：" + enrollment.getProgress() + "%", "course", courseId);
        return toResponse(userId, course);
    }

    private CourseResponse toResponse(Long userId, TrainingCourse course) {
        TrainingCourseEnrollment enrollment = enrollmentMapper.selectOne(new LambdaQueryWrapper<TrainingCourseEnrollment>()
                .eq(TrainingCourseEnrollment::getUserId, userId)
                .eq(TrainingCourseEnrollment::getCourseId, course.getId())
                .last("LIMIT 1"));
        List<CourseLessonResponse> lessons = lessonMapper.selectList(new LambdaQueryWrapper<TrainingCourseLesson>()
                        .eq(TrainingCourseLesson::getCourseId, course.getId())
                        .orderByAsc(TrainingCourseLesson::getSortOrder))
                .stream()
                .map(CourseLessonResponse::from)
                .toList();
        return CourseResponse.from(course,
                enrollment == null ? null : enrollment.getProgress(),
                enrollment == null ? "not_enrolled" : enrollment.getStatus(),
                lessons);
    }

    private TrainingCourse requireCourse(Long courseId) {
        TrainingCourse course = courseMapper.selectById(courseId);
        if (course == null || course.getStatus() == null || course.getStatus() == 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "课程不存在");
        }
        return course;
    }

    private void createSeed(String title, String audience, String category, String summary, int hours) {
        TrainingCourse course = new TrainingCourse();
        course.setTitle(title);
        course.setAudience(audience);
        course.setCategory(category);
        course.setSummary(summary);
        course.setCoverUrl("/uploads/mock/covers/course.png");
        course.setHours(hours);
        course.setStatus(1);
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
        courseMapper.insert(course);
        createLesson(course.getId(), "功能入口与场景理解", "认识本课程对应的平台功能和乡村课堂任务。", 1);
        createLesson(course.getId(), "真实案例演练", "基于本地案例完成一次完整操作。", 2);
        createLesson(course.getId(), "成果沉淀与反馈", "将操作成果保存到档案并提交课程反馈。", 3);
    }

    private void createLesson(Long courseId, String title, String content, int order) {
        TrainingCourseLesson lesson = new TrainingCourseLesson();
        lesson.setCourseId(courseId);
        lesson.setTitle(title);
        lesson.setContent(content);
        lesson.setVideoUrl("/uploads/mock/videos/course-" + order + ".mp4");
        lesson.setDuration(12);
        lesson.setSortOrder(order);
        lessonMapper.insert(lesson);
    }

    private String trimToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
}
