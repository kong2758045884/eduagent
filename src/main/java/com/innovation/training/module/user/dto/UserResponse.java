package com.innovation.training.module.user.dto;

import com.innovation.training.module.user.entity.User;

import java.time.LocalDateTime;

public class UserResponse {

    private Long id;
    private String username;
    private String nickname;
    private String role;
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
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponse from(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setRole(user.getRole());
        response.setTeacherType(user.getTeacherType());
        response.setCounty(user.getCounty());
        response.setSchool(user.getSchool());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setRealName(user.getRealName());
        response.setPhone(user.getPhone());
        response.setSubject(user.getSubject());
        response.setGrade(user.getGrade());
        response.setTitle(user.getTitle());
        response.setTeachingYears(user.getTeachingYears());
        response.setBio(user.getBio());
        response.setExpertiseTags(user.getExpertiseTags());
        response.setStatus(user.getStatus());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTeacherType() {
        return teacherType;
    }

    public void setTeacherType(String teacherType) {
        this.teacherType = teacherType;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTeachingYears() {
        return teachingYears;
    }

    public void setTeachingYears(Integer teachingYears) {
        this.teachingYears = teachingYears;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getExpertiseTags() {
        return expertiseTags;
    }

    public void setExpertiseTags(String expertiseTags) {
        this.expertiseTags = expertiseTags;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
