package com.innovation.training.support;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.innovation.training.common.BusinessException;
import com.innovation.training.common.ErrorCode;
import com.innovation.training.config.FileStorageProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

@Service
public class LocalFileStorageService {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    private final FileStorageProperties properties;

    public LocalFileStorageService(FileStorageProperties properties) {
        this.properties = properties;
    }

    public StoredFile store(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Upload file must not be empty");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Upload file must not exceed 10MB");
        }

        try {
            byte[] bytes = file.getBytes();
            return storeBytes(bytes,
                    StringUtils.hasText(file.getOriginalFilename()) ? file.getOriginalFilename() : "upload",
                    file.getContentType(), folder);
        } catch (IOException ex) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Failed to save uploaded file");
        }
    }

    public StoredFile storeBytes(byte[] bytes, String originalName, String contentType, String folder) {
        if (bytes == null || bytes.length == 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "File content must not be empty");
        }
        if (bytes.length > MAX_FILE_SIZE) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "File must not exceed 10MB");
        }
        String safeOriginalName = StringUtils.hasText(originalName) ? originalName : "generated";
        String extension = resolveExtension(safeOriginalName);
        LocalDate today = LocalDate.now();
        String relativeDir = "%s/%d/%02d".formatted(safeFolder(folder), today.getYear(), today.getMonthValue());
        String storedName = UUID.randomUUID() + extension;
        String relativePath = relativeDir + "/" + storedName;
        if ("oss".equalsIgnoreCase(properties.getStorageType())) {
            return storeToOss(bytes, safeOriginalName, contentType, relativePath);
        }
        Path root = Path.of(properties.getUploadRoot()).toAbsolutePath().normalize();
        Path targetDir = root.resolve(relativeDir).normalize();
        Path target = targetDir.resolve(storedName).normalize();
        if (!target.startsWith(root)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Illegal upload path");
        }
        try {
            Files.createDirectories(targetDir);
            Files.write(target, bytes);
            String publicUrl = properties.getPublicBaseUrl() + "/" + relativePath.replace("\\", "/");
            return new StoredFile(safeOriginalName, contentType, relativePath, publicUrl, bytes);
        } catch (IOException ex) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Failed to save uploaded file");
        }
    }

    private StoredFile storeToOss(byte[] bytes, String originalName, String contentType, String relativePath) {
        if (!StringUtils.hasText(properties.getOssEndpoint())
                || !StringUtils.hasText(properties.getOssBucketName())
                || !StringUtils.hasText(properties.getOssAccessKeyId())
                || !StringUtils.hasText(properties.getOssAccessKeySecret())) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "OSS configuration is incomplete");
        }
        OSS ossClient = new OSSClientBuilder().build(
                properties.getOssEndpoint(),
                properties.getOssAccessKeyId(),
                properties.getOssAccessKeySecret());
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bytes.length);
            if (StringUtils.hasText(contentType)) {
                metadata.setContentType(contentType);
            }
            ossClient.putObject(properties.getOssBucketName(), relativePath,
                    new ByteArrayInputStream(bytes), metadata);
            return new StoredFile(originalName, contentType, relativePath, resolveOssPublicUrl(relativePath), bytes);
        } catch (RuntimeException ex) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Failed to upload file to OSS");
        } finally {
            ossClient.shutdown();
        }
    }

    private String resolveOssPublicUrl(String relativePath) {
        if (StringUtils.hasText(properties.getOssCustomDomain())) {
            return trimTrailingSlash(properties.getOssCustomDomain()) + "/" + relativePath;
        }
        return trimTrailingSlash(properties.getOssEndpoint()).replace("://", "://" + properties.getOssBucketName() + ".")
                + "/" + relativePath;
    }

    private String trimTrailingSlash(String value) {
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }

    private String safeFolder(String folder) {
        if (!StringUtils.hasText(folder)) {
            return "misc";
        }
        return folder.replaceAll("[^a-zA-Z0-9_-]", "").toLowerCase(Locale.ROOT);
    }

    private String resolveExtension(String originalName) {
        int index = originalName.lastIndexOf('.');
        if (index < 0 || index == originalName.length() - 1) {
            return "";
        }
        String extension = originalName.substring(index).toLowerCase(Locale.ROOT);
        return extension.length() > 12 ? "" : extension;
    }
}
