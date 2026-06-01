package com.innovation.training.module.casebase.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.innovation.training.module.casebase.dto.CreateLocalTeachingCaseRequest;
import com.innovation.training.module.casebase.dto.LocalTeachingCaseResponse;
import com.innovation.training.module.casebase.entity.LocalTeachingCase;
import com.innovation.training.module.casebase.mapper.LocalTeachingCaseMapper;
import com.innovation.training.module.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class LocalTeachingCaseService {

    private final LocalTeachingCaseMapper mapper;

    public LocalTeachingCaseService(LocalTeachingCaseMapper mapper) {
        this.mapper = mapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public void seedIfEmpty() {
        Long count = mapper.selectCount(null);
        if (count != null && count > 0) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        insertSeed("水稻产量算百分比", "五年级", "百分比", "本地水稻亩产",
                "用今年和去年水稻亩产对比，设计百分比变化和增产率问题。", "百分比,水稻,产量", now);
        insertSeed("鸡鸭数量讲倍数", "三年级", "倍的认识", "村里家禽养殖",
                "用鸡鸭数量关系理解一份数、几份数和倍数表达。", "倍数,家禽,一份数", now);
        insertSeed("磨盘讲圆的周长", "六年级", "圆的周长", "村里石磨/磨盘",
                "测量磨盘直径和周长，理解圆周率和周长公式。", "圆,周长,磨盘", now);
        insertSeed("田埂测量讲周长", "四年级", "长方形周长", "田埂和菜地",
                "用菜地围栏长度估算长方形周长，联系实际测量。", "周长,田埂,菜地", now);
        insertSeed("集市称重讲单位换算", "四年级", "质量单位", "乡镇集市",
                "用斤、千克和袋装大米建立量感，解决单位换算。", "单位换算,斤,千克", now);
    }

    public List<LocalTeachingCaseResponse> list(String keyword, String subject, String grade) {
        seedIfEmpty();
        LambdaQueryWrapper<LocalTeachingCase> wrapper = new LambdaQueryWrapper<LocalTeachingCase>()
                .orderByDesc(LocalTeachingCase::getUsageCount)
                .orderByDesc(LocalTeachingCase::getUpdatedAt);
        if (StringUtils.hasText(subject)) {
            wrapper.eq(LocalTeachingCase::getSubject, subject.trim());
        }
        if (StringUtils.hasText(grade)) {
            wrapper.eq(LocalTeachingCase::getGrade, grade.trim());
        }
        if (StringUtils.hasText(keyword)) {
            String key = keyword.trim();
            wrapper.and(item -> item.like(LocalTeachingCase::getTitle, key)
                    .or().like(LocalTeachingCase::getKnowledgePoint, key)
                    .or().like(LocalTeachingCase::getLocalScenario, key)
                    .or().like(LocalTeachingCase::getTags, key)
                    .or().like(LocalTeachingCase::getContent, key));
        }
        return mapper.selectList(wrapper).stream().map(LocalTeachingCaseResponse::from).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public LocalTeachingCaseResponse create(CreateLocalTeachingCaseRequest request, User user) {
        LocalTeachingCase item = new LocalTeachingCase();
        item.setTitle(request.getTitle().trim());
        item.setSubject(defaultText(request.getSubject(), defaultText(user.getSubject(), "数学")));
        item.setGrade(defaultText(request.getGrade(), user.getGrade()));
        item.setKnowledgePoint(trimToNull(request.getKnowledgePoint()));
        item.setLocalScenario(trimToNull(request.getLocalScenario()));
        item.setResourceType(defaultText(request.getResourceType(), "scenario"));
        item.setContent(trimToNull(request.getContent()));
        item.setTags(trimToNull(request.getTags()));
        item.setCounty(defaultText(request.getCounty(), user.getCounty()));
        item.setSchool(defaultText(request.getSchool(), user.getSchool()));
        item.setUsageCount(0);
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        mapper.insert(item);
        return LocalTeachingCaseResponse.from(item);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<LocalTeachingCase> matchForPrompt(String requirement) {
        seedIfEmpty();
        List<LocalTeachingCase> all = mapper.selectList(new LambdaQueryWrapper<LocalTeachingCase>()
                .orderByDesc(LocalTeachingCase::getUsageCount)
                .orderByDesc(LocalTeachingCase::getUpdatedAt));
        String req = requirement == null ? "" : requirement.toLowerCase(Locale.ROOT);
        List<LocalTeachingCase> matched = all.stream()
                .sorted(Comparator.comparingInt(item -> -score(item, req)))
                .limit(3)
                .toList();
        matched.forEach(item -> {
            item.setUsageCount((item.getUsageCount() == null ? 0 : item.getUsageCount()) + 1);
            item.setUpdatedAt(LocalDateTime.now());
            mapper.updateById(item);
        });
        return matched;
    }

    private int score(LocalTeachingCase item, String req) {
        int score = 0;
        score += hit(req, item.getTitle()) * 5;
        score += hit(req, item.getKnowledgePoint()) * 4;
        score += hit(req, item.getLocalScenario()) * 3;
        score += hit(req, item.getTags()) * 2;
        score += hit(req, item.getGrade());
        return score;
    }

    private int hit(String req, String value) {
        if (!StringUtils.hasText(value) || !StringUtils.hasText(req)) {
            return 0;
        }
        String text = value.toLowerCase(Locale.ROOT);
        return req.contains(text) || text.chars().anyMatch(ch -> req.indexOf(ch) >= 0) ? 1 : 0;
    }

    private void insertSeed(String title, String grade, String knowledgePoint, String scenario,
                            String content, String tags, LocalDateTime now) {
        LocalTeachingCase item = new LocalTeachingCase();
        item.setTitle(title);
        item.setSubject("数学");
        item.setGrade(grade);
        item.setKnowledgePoint(knowledgePoint);
        item.setLocalScenario(scenario);
        item.setResourceType("scenario");
        item.setContent(content);
        item.setTags(tags);
        item.setCounty("演示县");
        item.setSchool("桥头小学");
        item.setUsageCount(0);
        item.setCreatedAt(now);
        item.setUpdatedAt(now);
        mapper.insert(item);
    }

    private String trimToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private String defaultText(String value, String fallback) {
        return StringUtils.hasText(value) ? value.trim() : fallback;
    }
}
