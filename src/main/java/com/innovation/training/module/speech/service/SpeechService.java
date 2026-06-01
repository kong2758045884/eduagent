package com.innovation.training.module.speech.service;

import com.innovation.training.module.ai.QwenSpeechClient;
import com.innovation.training.module.file.dto.FileResponse;
import com.innovation.training.module.file.service.AppFileService;
import com.innovation.training.module.speech.dto.SpeechTranscriptionResponse;
import com.innovation.training.support.LocalFileStorageService;
import com.innovation.training.support.StoredFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SpeechService {

    private final LocalFileStorageService fileStorageService;
    private final AppFileService appFileService;
    private final QwenSpeechClient qwenSpeechClient;

    public SpeechService(LocalFileStorageService fileStorageService,
                         AppFileService appFileService,
                         QwenSpeechClient qwenSpeechClient) {
        this.fileStorageService = fileStorageService;
        this.appFileService = appFileService;
        this.qwenSpeechClient = qwenSpeechClient;
    }

    @Transactional(rollbackFor = Exception.class)
    public SpeechTranscriptionResponse transcribe(Long userId, MultipartFile file, String language, String bizType, Long bizId) {
        StoredFile storedFile = fileStorageService.store(file, "speech");
        FileResponse fileResponse = appFileService.record(userId, storedFile,
                bizType == null || bizType.isBlank() ? "speech" : bizType, bizId);
        String transcript = qwenSpeechClient.transcribe(storedFile.getBytes(), storedFile.getContentType(), language);
        return new SpeechTranscriptionResponse(transcript, language == null || language.isBlank() ? "zh-CN" : language, fileResponse);
    }
}
