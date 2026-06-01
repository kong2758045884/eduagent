package com.innovation.training.module.classroom;

public class ClassroomAssetResponse {

    private String id;
    private String title;
    private String assetType;
    private String subject;
    private String grade;
    private String description;
    private String tags;
    private String version;
    private String demoUrl;
    private String offlinePackageUrl;
    private Long fileSize;
    private Boolean offlineReady;
    private Integer useCount;

    public ClassroomAssetResponse() {
    }

    public ClassroomAssetResponse(String id, String title, String assetType, String description, Boolean offlineReady) {
        this.id = id;
        this.title = title;
        this.assetType = assetType;
        this.description = description;
        this.offlineReady = offlineReady;
    }

    public static ClassroomAssetResponse from(ClassroomAsset asset) {
        ClassroomAssetResponse response = new ClassroomAssetResponse();
        response.setId(asset.getAssetKey());
        response.setTitle(asset.getTitle());
        response.setAssetType(asset.getAssetType());
        response.setSubject(asset.getSubject());
        response.setGrade(asset.getGrade());
        response.setDescription(asset.getDescription());
        response.setTags(asset.getTags());
        response.setVersion(asset.getVersion());
        response.setDemoUrl(asset.getDemoUrl());
        response.setOfflinePackageUrl(asset.getOfflinePackageUrl());
        response.setFileSize(asset.getFileSize());
        response.setOfflineReady(asset.getOfflineReady() != null && asset.getOfflineReady() == 1);
        response.setUseCount(asset.getUseCount());
        return response;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAssetType() { return assetType; }
    public void setAssetType(String assetType) { this.assetType = assetType; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getDemoUrl() { return demoUrl; }
    public void setDemoUrl(String demoUrl) { this.demoUrl = demoUrl; }
    public String getOfflinePackageUrl() { return offlinePackageUrl; }
    public void setOfflinePackageUrl(String offlinePackageUrl) { this.offlinePackageUrl = offlinePackageUrl; }
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public Boolean getOfflineReady() { return offlineReady; }
    public void setOfflineReady(Boolean offlineReady) { this.offlineReady = offlineReady; }
    public Integer getUseCount() { return useCount; }
    public void setUseCount(Integer useCount) { this.useCount = useCount; }
}
