package com.innovation.training.module.diagnosis.service;

import com.innovation.training.module.diagnosis.dto.CreateDiagnosisRequest;
import com.innovation.training.module.diagnosis.dto.DiagnosisResponse;
import com.innovation.training.support.StoredFile;

import java.util.List;

public interface DiagnosisService {

    DiagnosisResponse create(Long userId, CreateDiagnosisRequest request);

    DiagnosisResponse createWithImage(Long userId, CreateDiagnosisRequest request, StoredFile image);

    List<DiagnosisResponse> list(Long userId);

    List<StudentDiagnosisSummaryResponse> listStudents(Long userId, String className);

    StudentDiagnosisProfileResponse studentProfile(Long userId, String studentName, String className);

    ClassHeatmapResponse heatmap(Long userId, String className, Integer days);

    DiagnosisResponse archive(Long userId, Long diagnosisId, String note);
}
