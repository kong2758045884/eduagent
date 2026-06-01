package com.innovation.training.module.classroom;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.innovation.training.common.BusinessException;
import com.innovation.training.common.ErrorCode;
import com.innovation.training.module.growth.service.GrowthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClassroomAssetService {

    private final ClassroomAssetMapper mapper;
    private final GrowthService growthService;

    public ClassroomAssetService(ClassroomAssetMapper mapper, GrowthService growthService) {
        this.mapper = mapper;
        this.growthService = growthService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void seedIfEmpty() {
        Long count = mapper.selectCount(null);
        if (count != null && count > 0) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        insert("fraction-pie", "分数饼图分割", "math-animation", "三年级", "分数,等分",
                "用于演示分数含义、等分和合并。", "/assets/classroom/fraction-pie/index.html", "/assets/classroom/fraction-pie-offline.zip", now);
        insert("cylinder-net", "圆柱体展开", "webgl-model", "六年级", "圆柱,表面积",
                "用于演示圆柱侧面展开与表面积。", "/assets/classroom/cylinder-net/index.html", "/assets/classroom/cylinder-net-offline.zip", now);
        insert("line-segment", "线段图关系分析", "h5-tool", "五年级", "线段图,应用题",
                "用于分数应用题和数量关系建模。", "/assets/classroom/line-segment/index.html", "/assets/classroom/line-segment-offline.zip", now);
        insert("chicken-rabbit", "鸡兔同笼操作板", "h5-tool", "五年级", "鸡兔同笼,枚举",
                "用于枚举、假设和方程思想演示。", "/assets/classroom/chicken-rabbit/index.html", "/assets/classroom/chicken-rabbit-offline.zip", now);
    }

    public List<ClassroomAssetResponse> list() {
        seedIfEmpty();
        return mapper.selectList(new LambdaQueryWrapper<ClassroomAsset>()
                        .orderByDesc(ClassroomAsset::getUseCount)
                        .orderByDesc(ClassroomAsset::getUpdatedAt))
                .stream()
                .map(ClassroomAssetResponse::from)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public ClassroomAssetResponse use(Long userId, String assetKey) {
        seedIfEmpty();
        ClassroomAsset asset = mapper.selectOne(new LambdaQueryWrapper<ClassroomAsset>()
                .eq(ClassroomAsset::getAssetKey, assetKey)
                .last("LIMIT 1"));
        if (asset == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "课堂资源包不存在");
        }
        asset.setUseCount((asset.getUseCount() == null ? 0 : asset.getUseCount()) + 1);
        asset.setUpdatedAt(LocalDateTime.now());
        mapper.updateById(asset);
        growthService.record(userId, "classroom_asset_used",
                "使用课堂辅助资源：" + asset.getTitle(), asset.getDescription(), "classroom_asset", asset.getId());
        return ClassroomAssetResponse.from(asset);
    }

    private void insert(String key, String title, String type, String grade, String tags,
                        String description, String demoUrl, String offlineUrl, LocalDateTime now) {
        ClassroomAsset asset = new ClassroomAsset();
        asset.setAssetKey(key);
        asset.setTitle(title);
        asset.setAssetType(type);
        asset.setSubject("数学");
        asset.setGrade(grade);
        asset.setDescription(description);
        asset.setTags(tags);
        asset.setVersion("1.0.0");
        asset.setDemoUrl(demoUrl);
        asset.setOfflinePackageUrl(offlineUrl);
        asset.setFileSize(2_048_000L);
        asset.setOfflineReady(1);
        asset.setUseCount(0);
        asset.setCreatedAt(now);
        asset.setUpdatedAt(now);
        mapper.insert(asset);
    }
}
