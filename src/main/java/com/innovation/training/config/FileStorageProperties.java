package com.innovation.training.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    private String uploadRoot = "uploads";

    private String publicBaseUrl = "/uploads";

    private String storageType = "local";

    private String ossEndpoint;

    private String ossBucketName;

    private String ossAccessKeyId;

    private String ossAccessKeySecret;

    private String ossCustomDomain;

    public String getUploadRoot() {
        return uploadRoot;
    }

    public void setUploadRoot(String uploadRoot) {
        this.uploadRoot = uploadRoot;
    }

    public Path resolveUploadRoot() {
        Path configured = Path.of(uploadRoot);
        if (configured.isAbsolute()) {
            return configured.normalize();
        }
        return resolveApplicationDirectory().resolve(configured).normalize();
    }

    private Path resolveApplicationDirectory() {
        try {
            Path codeSource = Path.of(FileStorageProperties.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()).toAbsolutePath().normalize();
            if (Files.isRegularFile(codeSource)) {
                return codeSource.getParent();
            }
        } catch (URISyntaxException | RuntimeException ignored) {
            // Fall back to the process working directory below.
        }
        return Path.of("").toAbsolutePath().normalize();
    }

    public String getPublicBaseUrl() {
        return publicBaseUrl;
    }

    public void setPublicBaseUrl(String publicBaseUrl) {
        this.publicBaseUrl = publicBaseUrl;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getOssEndpoint() {
        return ossEndpoint;
    }

    public void setOssEndpoint(String ossEndpoint) {
        this.ossEndpoint = ossEndpoint;
    }

    public String getOssBucketName() {
        return ossBucketName;
    }

    public void setOssBucketName(String ossBucketName) {
        this.ossBucketName = ossBucketName;
    }

    public String getOssAccessKeyId() {
        return ossAccessKeyId;
    }

    public void setOssAccessKeyId(String ossAccessKeyId) {
        this.ossAccessKeyId = ossAccessKeyId;
    }

    public String getOssAccessKeySecret() {
        return ossAccessKeySecret;
    }

    public void setOssAccessKeySecret(String ossAccessKeySecret) {
        this.ossAccessKeySecret = ossAccessKeySecret;
    }

    public String getOssCustomDomain() {
        return ossCustomDomain;
    }

    public void setOssCustomDomain(String ossCustomDomain) {
        this.ossCustomDomain = ossCustomDomain;
    }
}
