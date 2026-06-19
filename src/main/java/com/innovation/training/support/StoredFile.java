package com.innovation.training.support;

public class StoredFile {

    private final String originalName;
    private final String contentType;
    private final String relativePath;
    private final String publicUrl;
    private final long size;
    private final byte[] bytes;

    public StoredFile(String originalName, String contentType, String relativePath, String publicUrl, byte[] bytes) {
        this(originalName, contentType, relativePath, publicUrl, bytes == null ? 0 : bytes.length, bytes);
    }

    public StoredFile(String originalName, String contentType, String relativePath, String publicUrl, long size) {
        this(originalName, contentType, relativePath, publicUrl, size, null);
    }

    public StoredFile(String originalName, String contentType, String relativePath, String publicUrl, long size, byte[] bytes) {
        this.originalName = originalName;
        this.contentType = contentType;
        this.relativePath = relativePath;
        this.publicUrl = publicUrl;
        this.size = size;
        this.bytes = bytes;
    }

    public String getOriginalName() { return originalName; }

    public String getContentType() { return contentType; }

    public String getRelativePath() { return relativePath; }

    public String getPublicUrl() { return publicUrl; }

    public long getSize() { return size; }

    public byte[] getBytes() { return bytes; }
}
