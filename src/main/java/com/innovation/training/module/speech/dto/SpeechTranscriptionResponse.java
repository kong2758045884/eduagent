package com.innovation.training.module.speech.dto;

import com.innovation.training.module.file.dto.FileResponse;

public class SpeechTranscriptionResponse {

    private String transcript;
    private String language;
    private FileResponse file;

    public SpeechTranscriptionResponse() {
    }

    public SpeechTranscriptionResponse(String transcript, String language, FileResponse file) {
        this.transcript = transcript;
        this.language = language;
        this.file = file;
    }

    public String getTranscript() { return transcript; }
    public void setTranscript(String transcript) { this.transcript = transcript; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public FileResponse getFile() { return file; }
    public void setFile(FileResponse file) { this.file = file; }
}
