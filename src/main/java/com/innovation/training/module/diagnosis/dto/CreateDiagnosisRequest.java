package com.innovation.training.module.diagnosis.dto;

public class CreateDiagnosisRequest {

    private String studentName;

    private String className;

    private String subject = "数学";

    private String topic;

    private String questionText;

    private String answerText;

    private String imageUrl;

    private String imageNote;

    public String getStudentName() { return studentName; }

    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getClassName() { return className; }

    public void setClassName(String className) { this.className = className; }

    public String getSubject() { return subject; }

    public void setSubject(String subject) { this.subject = subject; }

    public String getTopic() { return topic; }

    public void setTopic(String topic) { this.topic = topic; }

    public String getQuestionText() { return questionText; }

    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getAnswerText() { return answerText; }

    public void setAnswerText(String answerText) { this.answerText = answerText; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getImageNote() { return imageNote; }

    public void setImageNote(String imageNote) { this.imageNote = imageNote; }
}
