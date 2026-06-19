package com.innovation.training.support;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.innovation.training.common.BusinessException;
import com.innovation.training.common.ErrorCode;
import com.innovation.training.config.FileStorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

@Service
public class LocalFileStorageService {

    private static final Logger log = LoggerFactory.getLogger(LocalFileStorageService.class);

    private static final long MAX_FILE_SIZE = 500L * 1024 * 1024;

    private final FileStorageProperties properties;

    public LocalFileStorageService(FileStorageProperties properties) {
        this.properties = properties;
    }

    public StoredFile store(MultipartFile file, String folder) {
        validateMultipart(file);
        String originalName = safeOriginalName(file.getOriginalFilename(), "upload");
        String relativePath = buildRelativePath(originalName, folder);
        if ("oss".equalsIgnoreCase(properties.getStorageType())) {
            return storeMultipartToOss(file, originalName, file.getContentType(), relativePath);
        }
        return storeMultipartToLocal(file, originalName, file.getContentType(), relativePath);
    }

    public StoredFile storeWithBytes(MultipartFile file, String folder) {
        validateMultipart(file);
        try {
            return storeBytes(
                    file.getBytes(),
                    safeOriginalName(file.getOriginalFilename(), "upload"),
                    file.getContentType(),
                    folder);
        } catch (IOException ex) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Failed to read uploaded file");
        }
    }

    public StoredFile storeBytes(byte[] bytes, String originalName, String contentType, String folder) {
        validateBytes(bytes);
        String safeName = safeOriginalName(originalName, "generated");
        String relativePath = buildRelativePath(safeName, folder);
        if ("oss".equalsIgnoreCase(properties.getStorageType())) {
            return storeToOss(bytes, safeName, contentType, relativePath);
        }
        return storeBytesToLocal(bytes, safeName, contentType, relativePath);
    }

    private void validateMultipart(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Upload file must not be empty");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Upload file must not exceed 500MB");
        }
    }

    private void validateBytes(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "File content must not be empty");
        }
        if (bytes.length > MAX_FILE_SIZE) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "File must not exceed 500MB");
        }
    }

    private String buildRelativePath(String originalName, String folder) {
        LocalDate today = LocalDate.now();
        String relativeDir = "%s/%d/%02d".formatted(safeFolder(folder), today.getYear(), today.getMonthValue());
        return relativeDir + "/" + UUID.randomUUID() + resolveExtension(originalName);
    }

    private StoredFile storeMultipartToLocal(MultipartFile file, String originalName, String contentType, String relativePath) {
        Path target = resolveLocalTarget(relativePath);
        try {
            Files.createDirectories(target.getParent());
            try (InputStream input = file.getInputStream()) {
                Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return new StoredFile(originalName, contentType, relativePath, resolveLocalPublicUrl(relativePath), file.getSize());
        } catch (IOException ex) {
            throw localStorageFailure("Failed to save uploaded file", target, ex);
        }
    }

    private StoredFile storeBytesToLocal(byte[] bytes, String originalName, String contentType, String relativePath) {
        Path target = resolveLocalTarget(relativePath);
        try {
            Files.createDirectories(target.getParent());
            Files.write(target, bytes);
            return new StoredFile(originalName, contentType, relativePath, resolveLocalPublicUrl(relativePath), bytes.length, bytes);
        } catch (IOException ex) {
            throw localStorageFailure("Failed to save generated file", target, ex);
        }
    }

    private Path resolveLocalTarget(String relativePath) {
        Path root = prepareUploadRoot();
        Path target = root.resolve(relativePath).normalize();
        if (!target.startsWith(root)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Illegal upload path");
        }
        return target;
    }

    private Path prepareUploadRoot() {
        Path root = properties.resolveUploadRoot();
        try {
            Files.createDirectories(root);
        } catch (IOException ex) {
            throw localStorageFailure("Failed to create upload directory", root, ex);
        }
        if (!Files.isDirectory(root)) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Upload path is not a directory: " + root);
        }
        if (!Files.isWritable(root)) {
            log.error("Upload directory is not writable: {}", root);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Upload directory is not writable: " + root);
        }
        return root;
    }

    private BusinessException localStorageFailure(String action, Path path, Exception ex) {
        log.error("{}: {}", action, path, ex);
        String reason = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
        return new BusinessException(ErrorCode.INTERNAL_ERROR, action + ": " + path + " (" + reason + ")");
    }

    private String resolveLocalPublicUrl(String relativePath) {
        String baseUrl = StringUtils.hasText(properties.getPublicBaseUrl()) ? properties.getPublicBaseUrl() : "/uploads";
        return trimTrailingSlash(baseUrl) + "/" + relativePath.replace("\\", "/");
    }

    private StoredFile storeMultipartToOss(MultipartFile file, String originalName, String contentType, String relativePath) {
        ensureOssConfigured();
        OSS ossClient = new OSSClientBuilder().build(
                properties.getOssEndpoint(),
                properties.getOssAccessKeyId(),
                properties.getOssAccessKeySecret());
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            if (StringUtils.hasText(contentType)) {
                metadata.setContentType(contentType);
            }
            try (InputStream input = file.getInputStream()) {
                ossClient.putObject(properties.getOssBucketName(), relativePath, input, metadata);
            }
            return new StoredFile(originalName, contentType, relativePath, resolveOssPublicUrl(relativePath), file.getSize());
        } catch (IOException | RuntimeException ex) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Failed to upload file to OSS");
        } finally {
            ossClient.shutdown();
        }
    }

    private StoredFile storeToOss(byte[] bytes, String originalName, String contentType, String relativePath) {
        ensureOssConfigured();
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
            return new StoredFile(originalName, contentType, relativePath, resolveOssPublicUrl(relativePath), bytes.length, bytes);
        } catch (RuntimeException ex) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Failed to upload file to OSS");
        } finally {
            ossClient.shutdown();
        }
    }

    private void ensureOssConfigured() {
        if (!StringUtils.hasText(properties.getOssEndpoint())
                || !StringUtils.hasText(properties.getOssBucketName())
                || !StringUtils.hasText(properties.getOssAccessKeyId())
                || !StringUtils.hasText(properties.getOssAccessKeySecret())) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "OSS configuration is incomplete");
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

    private String safeOriginalName(String originalName, String fallback) {
        return StringUtils.hasText(originalName) ? originalName : fallback;
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
