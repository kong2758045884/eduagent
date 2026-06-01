package com.innovation.training.module.report.dto;

import com.innovation.training.module.file.dto.FileResponse;

public class ReportResponse {

    private String reportType;
    private FileResponse file;

    public ReportResponse() {
    }

    public ReportResponse(String reportType, FileResponse file) {
        this.reportType = reportType;
        this.file = file;
    }

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    public FileResponse getFile() { return file; }
    public void setFile(FileResponse file) { this.file = file; }
}
