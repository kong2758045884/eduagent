package com.innovation.training.module.profile.dto;

public class UpdateProfileRequest {

    private String nickname;
    private String teacherType;
    private String county;
    private String school;
    private String avatarUrl;
    private String realName;
    private String phone;
    private String subject;
    private String grade;
    private String title;
    private Integer teachingYears;
    private String bio;
    private String expertiseTags;

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getTeacherType() { return teacherType; }
    public void setTeacherType(String teacherType) { this.teacherType = teacherType; }
    public String getCounty() { return county; }
    public void setCounty(String county) { this.county = county; }
    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getTeachingYears() { return teachingYears; }
    public void setTeachingYears(Integer teachingYears) { this.teachingYears = teachingYears; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getExpertiseTags() { return expertiseTags; }
    public void setExpertiseTags(String expertiseTags) { this.expertiseTags = expertiseTags; }
}
