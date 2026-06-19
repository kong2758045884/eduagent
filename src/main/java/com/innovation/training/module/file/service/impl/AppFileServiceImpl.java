package com.innovation.training.module.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.innovation.training.module.file.dto.FileResponse;
import com.innovation.training.module.file.entity.AppFile;
import com.innovation.training.module.file.mapper.AppFileMapper;
import com.innovation.training.module.file.service.AppFileService;
import com.innovation.training.support.LocalFileStorageService;
import com.innovation.training.support.StoredFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppFileServiceImpl implements AppFileService {

    private final AppFileMapper appFileMapper;
    private final LocalFileStorageService localFileStorageService;

    public AppFileServiceImpl(AppFileMapper appFileMapper, LocalFileStorageService localFileStorageService) {
        this.appFileMapper = appFileMapper;
        this.localFileStorageService = localFileStorageService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileResponse upload(Long userId, MultipartFile file, String bizType, Long bizId) {
        StoredFile storedFile = localFileStorageService.store(file, StringUtils.hasText(bizType) ? bizType : "misc");
        return record(userId, storedFile, bizType, bizId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileResponse record(Long userId, StoredFile storedFile, String bizType, Long bizId) {
        AppFile file = new AppFile();
        file.setUserId(userId);
        file.setOriginalName(storedFile.getOriginalName());
        file.setContentType(storedFile.getContentType());
        file.setRelativePath(storedFile.getRelativePath());
        file.setPublicUrl(storedFile.getPublicUrl());
        file.setSize(storedFile.getSize());
        file.setBizType(StringUtils.hasText(bizType) ? bizType : "misc");
        file.setBizId(bizId);
        file.setCreatedAt(LocalDateTime.now());
        appFileMapper.insert(file);
        return FileResponse.from(file);
    }

    @Override
    public List<FileResponse> list(Long userId, String bizType, Long bizId) {
        LambdaQueryWrapper<AppFile> wrapper = new LambdaQueryWrapper<AppFile>()
                .eq(AppFile::getUserId, userId)
                .orderByDesc(AppFile::getCreatedAt);
        if (StringUtils.hasText(bizType)) {
            wrapper.eq(AppFile::getBizType, bizType);
        }
        if (bizId != null) {
            wrapper.eq(AppFile::getBizId, bizId);
        }
        return appFileMapper.selectList(wrapper).stream().map(FileResponse::from).toList();
    }
}
