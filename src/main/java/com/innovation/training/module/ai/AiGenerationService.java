package com.innovation.training.module.ai;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.StringJoiner;

@Service
public class AiGenerationService {

    private static final String JSON_ONLY = "只返回 JSON，不要返回 Markdown，不要使用 ``` 包裹。";

    private final QwenChatClient qwenChatClient;

    public AiGenerationService(QwenChatClient qwenChatClient) {
        this.qwenChatClient = qwenChatClient;
    }

    public String generateLesson(String requirement) {
        String systemPrompt = """
                你是乡村数学教学智能体，服务对象是乡村学校数学教师。
                目标是把教师的自然语言备课需求转换为结构化教案草稿。
                输出必须贴合乡土情境，语言简洁可直接给前端展示。
                """ + JSON_ONLY;
        String userPrompt = """
                请根据下面的备课需求生成 JSON：
                字段：
                title: 教案标题
                summary: 一句话摘要
                grade: 年级
                subject: 学科
                knowledgePoint: 知识点
                localScenario: 乡土案例
                objectives: 字符串数组
                keyPoints: 字符串数组
                process: 数组，每项包含 name、duration、activities
                assessment: 字符串数组
                extension: 字符串数组
                
                备课需求：
                """ + requirement;
        return qwenChatClient.chat(systemPrompt, userPrompt);
    }

    public String diagnoseMistake(String questionText, String answerText, String imageNote) {
        String systemPrompt = """
                你是小学/初中数学认知诊断智能体。
                你不能只判断对错，而要定位学生出错步骤、认知根因，并给乡村课堂可落地的干预方法。
                """ + JSON_ONLY;
        String userPrompt = """
                请根据错题信息输出 JSON：
                字段：
                errorSteps: 字符串数组，指出可能的错误步骤
                rootCause: 字符串，说明核心认知根因
                interventions: 字符串数组，给 2-3 条可操作干预方法
                teacherScript: 字符串，给教师可直接说的话术
                followUpExercise: 字符串，给一个同类巩固题
                
                题目/识别文本：%s
                学生答案/错误表现：%s
                图片备注：%s
                """.formatted(nullToEmpty(questionText), nullToEmpty(answerText), nullToEmpty(imageNote));
        return qwenChatClient.chat(systemPrompt, userPrompt);
    }

    public String diagnoseMistakeWithImage(String questionText, String answerText, String imageNote,
                                           byte[] imageBytes, String contentType) {
        String systemPrompt = """
                你是小学/初中数学认知诊断智能体。
                你会读取错题图片，并结合教师输入的文本，识别题目、学生答案、错误步骤和认知根因。
                如果图片不清晰，要明确说明不确定性，并优先依据教师提供的文本诊断。
                """ + JSON_ONLY;
        String userPrompt = """
                请根据图片和文本输出 JSON：
                字段：
                recognizedQuestion: 字符串，识别出的题目
                recognizedAnswer: 字符串，识别出的学生答案或错误表现
                errorSteps: 字符串数组，指出可能的错误步骤
                rootCause: 字符串，说明核心认知根因
                interventions: 字符串数组，给 2-3 条可操作干预方法
                teacherScript: 字符串，给教师可直接说的话术
                followUpExercise: 字符串，给一个同类巩固题
                
                教师录入题目/OCR 文本：%s
                教师录入学生答案/错误表现：%s
                图片备注：%s
                """.formatted(nullToEmpty(questionText), nullToEmpty(answerText), nullToEmpty(imageNote));
        return qwenChatClient.chatWithImage(systemPrompt, userPrompt, imageBytes, contentType);
    }

    public String recommendResearchTopic(List<String> sources, String teacherGoal) {
        StringJoiner joiner = new StringJoiner("\n- ", "- ", "");
        sources.forEach(joiner::add);
        String systemPrompt = """
                你是乡村数学教研课题规划智能体。
                你需要把教师日常教案、错题、反思转化为可申报的小课题建议。
                """ + JSON_ONLY;
        String userPrompt = """
                请基于来源材料输出 JSON：
                字段：
                title: 课题名称
                value: 选题价值
                evidence: 字符串数组，说明数据支撑
                objectives: 字符串数组，研究目标
                plan: 字符串数组，研究步骤
                applicationDraft: 字符串，课题申报书初稿
                
                教师目标：%s
                来源材料：
                %s
                """.formatted(nullToEmpty(teacherGoal), sources == null || sources.isEmpty() ? "暂无来源材料" : joiner.toString());
        return qwenChatClient.chat(systemPrompt, userPrompt);
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
