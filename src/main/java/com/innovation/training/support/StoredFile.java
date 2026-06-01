package com.innovation.training.support;

public class StoredFile {

    private final String originalName;
    private final String contentType;
    private final String relativePath;
    private final String publicUrl;
    private final byte[] bytes;

    public StoredFile(String originalName, String contentType, String relativePath, String publicUrl, byte[] bytes) {
        this.originalName = originalName;
        this.contentType = contentType;
        this.relativePath = relativePath;
        this.publicUrl = publicUrl;
        this.bytes = bytes;
    }

    public String getOriginalName() { return originalName; }

    public String getContentType() { return contentType; }

    public String getRelativePath() { return relativePath; }

    public String getPublicUrl() { return publicUrl; }

    public byte[] getBytes() { return bytes; }
}
