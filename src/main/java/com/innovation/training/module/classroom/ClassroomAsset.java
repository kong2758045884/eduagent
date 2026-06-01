package com.innovation.training.module.classroom;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("classroom_asset")
public class ClassroomAsset {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("asset_key")
    private String assetKey;

    private String title;

    @TableField("asset_type")
    private String assetType;

    private String subject;
    private String grade;
    private String description;
    private String tags;
    private String version;

    @TableField("demo_url")
    private String demoUrl;

    @TableField("offline_package_url")
    private String offlinePackageUrl;

    @TableField("file_size")
    private Long fileSize;

    @TableField("offline_ready")
    private Integer offlineReady;

    @TableField("use_count")
    private Integer useCount;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAssetKey() { return assetKey; }
    public void setAssetKey(String assetKey) { this.assetKey = assetKey; }
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
    public Integer getOfflineReady() { return offlineReady; }
    public void setOfflineReady(Integer offlineReady) { this.offlineReady = offlineReady; }
    public Integer getUseCount() { return useCount; }
    public void setUseCount(Integer useCount) { this.useCount = useCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
