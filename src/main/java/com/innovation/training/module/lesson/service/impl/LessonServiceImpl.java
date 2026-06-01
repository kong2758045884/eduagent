package com.innovation.training.module.lesson.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovation.training.common.BusinessException;
import com.innovation.training.common.ErrorCode;
import com.innovation.training.module.ai.AiGenerationService;
import com.innovation.training.module.casebase.entity.LocalTeachingCase;
import com.innovation.training.module.casebase.service.LocalTeachingCaseService;
import com.innovation.training.module.lesson.dto.CreateReflectionRequest;
import com.innovation.training.module.lesson.dto.GenerateLessonRequest;
import com.innovation.training.module.lesson.dto.GenerateLessonResponse;
import com.innovation.training.module.lesson.dto.LessonResponse;
import com.innovation.training.module.lesson.dto.ReflectionResponse;
import com.innovation.training.module.lesson.dto.SaveLessonRequest;
import com.innovation.training.module.lesson.entity.LessonDraft;
import com.innovation.training.module.lesson.entity.LessonReflection;
import com.innovation.training.module.lesson.mapper.LessonDraftMapper;
import com.innovation.training.module.lesson.mapper.LessonReflectionMapper;
import com.innovation.training.module.lesson.service.LessonService;
import com.innovation.training.module.growth.service.GrowthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonDraftMapper lessonDraftMapper;
    private final LessonReflectionMapper reflectionMapper;
    private final AiGenerationService aiGenerationService;
    private final ObjectMapper objectMapper;
    private final GrowthService growthService;
    private final LocalTeachingCaseService localTeachingCaseService;

    public LessonServiceImpl(LessonDraftMapper lessonDraftMapper,
                             LessonReflectionMapper reflectionMapper,
                             AiGenerationService aiGenerationService,
                             ObjectMapper objectMapper,
                             GrowthService growthService,
                             LocalTeachingCaseService localTeachingCaseService) {
        this.lessonDraftMapper = lessonDraftMapper;
        this.reflectionMapper = reflectionMapper;
        this.aiGenerationService = aiGenerationService;
        this.objectMapper = objectMapper;
        this.growthService = growthService;
        this.localTeachingCaseService = localTeachingCaseService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GenerateLessonResponse generate(Long userId, GenerateLessonRequest request) {
        List<LocalTeachingCase> cases = localTeachingCaseService.matchForPrompt(request.getRequirement());
        String contentJson = stripCodeFence(aiGenerationService.generateLesson(buildRequirementWithCases(request.getRequirement(), cases)));
        String markdown = toLessonMarkdown(contentJson, request.getRequirement());
        LessonResponse saved = null;
        if (Boolean.TRUE.equals(request.getSave())) {
            SaveLessonRequest saveRequest = new SaveLessonRequest();
            saveRequest.setTitle(readText(contentJson, "title", "AI 生成教案"));
            saveRequest.setSummary(readText(contentJson, "summary", "结构化教案草稿"));
            saveRequest.setRequirement(request.getRequirement());
            saveRequest.setContent(markdown);
            saveRequest.setContentJson(contentJson);
            saved = saveLesson(userId, saveRequest);
        }
        return new GenerateLessonResponse(contentJson, markdown, saved);
    }

    private String buildRequirementWithCases(String requirement, List<LocalTeachingCase> cases) {
        if (cases == null || cases.isEmpty()) {
            return requirement;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(requirement == null ? "" : requirement).append("\n\n可优先参考以下数据库乡土案例：\n");
        for (LocalTeachingCase item : cases) {
            builder.append("- ").append(item.getTitle())
                    .append("｜").append(item.getGrade())
                    .append("｜").append(item.getKnowledgePoint())
                    .append("｜").append(item.getLocalScenario())
                    .append("：").append(item.getContent())
                    .append("\n");
        }
        return builder.toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LessonResponse saveLesson(Long userId, SaveLessonRequest request) {
        LessonDraft lesson = new LessonDraft();
        lesson.setUserId(userId);
        lesson.setTitle(request.getTitle().trim());
        lesson.setSummary(trimToNull(request.getSummary()));
        lesson.setRequirement(trimToNull(request.getRequirement()));
        lesson.setContent(request.getContent());
        lesson.setContentJson(trimToNull(request.getContentJson()));
        LocalDateTime now = LocalDateTime.now();
        lesson.setCreatedAt(now);
        lesson.setUpdatedAt(now);
        lessonDraftMapper.insert(lesson);
        growthService.record(userId, "lesson_saved", "保存教案：" + lesson.getTitle(),
                lesson.getSummary(), "lesson", lesson.getId());
        return LessonResponse.from(lesson);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LessonResponse updateLesson(Long userId, Long id, SaveLessonRequest request) {
        LessonDraft lesson = requireOwnedLesson(userId, id);
        lesson.setTitle(request.getTitle().trim());
        lesson.setSummary(trimToNull(request.getSummary()));
        lesson.setRequirement(trimToNull(request.getRequirement()));
        lesson.setContent(request.getContent());
        lesson.setContentJson(trimToNull(request.getContentJson()));
        lesson.setUpdatedAt(LocalDateTime.now());
        lessonDraftMapper.updateById(lesson);
        return LessonResponse.from(lesson);
    }

    @Override
    public List<LessonResponse> listLessons(Long userId) {
        return lessonDraftMapper.selectList(new LambdaQueryWrapper<LessonDraft>()
                        .eq(LessonDraft::getUserId, userId)
                        .orderByDesc(LessonDraft::getUpdatedAt))
                .stream()
                .map(LessonResponse::from)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReflectionResponse createReflection(Long userId, CreateReflectionRequest request) {
        requireOwnedLesson(userId, request.getLessonId());
        LessonReflection reflection = new LessonReflection();
        reflection.setUserId(userId);
        reflection.setLessonId(request.getLessonId());
        reflection.setText(request.getText().trim());
        reflection.setAiSummary(buildReflectionSummary(request.getText()));
        reflection.setShared(Boolean.TRUE.equals(request.getShared()) ? 1 : 0);
        reflection.setCreatedAt(LocalDateTime.now());
        reflectionMapper.insert(reflection);
        growthService.record(userId, "reflection_created", "记录课后反思",
                reflection.getText(), "reflection", reflection.getId());
        return ReflectionResponse.from(reflection);
    }

    @Override
    public List<ReflectionResponse> listReflections(Long userId, Long lessonId) {
        LambdaQueryWrapper<LessonReflection> wrapper = new LambdaQueryWrapper<LessonReflection>()
                .eq(LessonReflection::getUserId, userId)
                .orderByDesc(LessonReflection::getCreatedAt);
        if (lessonId != null) {
            wrapper.eq(LessonReflection::getLessonId, lessonId);
        }
        return reflectionMapper.selectList(wrapper).stream()
                .map(ReflectionResponse::from)
                .toList();
    }

    private LessonDraft requireOwnedLesson(Long userId, Long id) {
        LessonDraft lesson = lessonDraftMapper.selectOne(new LambdaQueryWrapper<LessonDraft>()
                .eq(LessonDraft::getId, id)
                .eq(LessonDraft::getUserId, userId)
                .last("LIMIT 1"));
        if (lesson == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "教案不存在");
        }
        return lesson;
    }

    private String toLessonMarkdown(String contentJson, String requirement) {
        try {
            JsonNode root = objectMapper.readTree(contentJson);
            StringBuilder builder = new StringBuilder();
            builder.append("# ").append(root.path("title").asText("AI 生成教案")).append("\n\n");
            builder.append("## 一、基本信息\n");
            builder.append("- 年级：").append(root.path("grade").asText("待确认")).append("\n");
            builder.append("- 学科：").append(root.path("subject").asText("数学")).append("\n");
            builder.append("- 知识点：").append(root.path("knowledgePoint").asText("待确认")).append("\n");
            builder.append("- 乡土案例：").append(root.path("localScenario").asText("结合本地生活情境")).append("\n\n");
            appendArray(builder, "## 二、教学目标", root.path("objectives"));
            appendArray(builder, "## 三、重难点", root.path("keyPoints"));
            builder.append("## 四、教学过程\n");
            JsonNode process = root.path("process");
            if (process.isArray()) {
                for (JsonNode node : process) {
                    builder.append("### ").append(node.path("name").asText("教学环节")).append("\n");
                    builder.append("- 时间：").append(node.path("duration").asText("")).append("\n");
                    builder.append("- 活动：").append(node.path("activities").asText("")).append("\n\n");
                }
            }
            appendArray(builder, "## 五、评价设计", root.path("assessment"));
            appendArray(builder, "## 六、课后延伸", root.path("extension"));
            builder.append("## 七、原始需求\n").append(requirement).append("\n");
            return builder.toString();
        } catch (Exception ex) {
            return "# AI 生成教案\n\n" + contentJson + "\n\n## 原始需求\n" + requirement;
        }
    }

    private void appendArray(StringBuilder builder, String title, JsonNode array) {
        builder.append(title).append("\n");
        if (array.isArray()) {
            for (JsonNode item : array) {
                builder.append("- ").append(item.asText()).append("\n");
            }
        }
        builder.append("\n");
    }

    private String readText(String contentJson, String field, String fallback) {
        try {
            JsonNode root = objectMapper.readTree(contentJson);
            String value = root.path(field).asText();
            return StringUtils.hasText(value) ? value : fallback;
        } catch (Exception ex) {
            return fallback;
        }
    }

    private String buildReflectionSummary(String text) {
        List<String> pieces = new ArrayList<>();
        pieces.add("课堂反思已关联到教案");
        if (text.length() > 40) {
            pieces.add(text.substring(0, 40) + "...");
        } else {
            pieces.add(text);
        }
        return String.join("：", pieces);
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
