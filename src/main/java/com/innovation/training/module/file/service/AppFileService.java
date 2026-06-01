package com.innovation.training.module.file.service;

import com.innovation.training.module.file.dto.FileResponse;
import com.innovation.training.support.StoredFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AppFileService {

    FileResponse upload(Long userId, MultipartFile file, String bizType, Long bizId);

    FileResponse record(Long userId, StoredFile storedFile, String bizType, Long bizId);

    List<FileResponse> list(Long userId, String bizType, Long bizId);
}
