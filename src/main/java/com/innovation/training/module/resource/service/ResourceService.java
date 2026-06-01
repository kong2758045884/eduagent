package com.innovation.training.module.resource.service;

import com.innovation.training.module.resource.dto.CreateResourceRequest;
import com.innovation.training.module.resource.dto.CreateCommentRequest;
import com.innovation.training.module.resource.dto.CommentResponse;
import com.innovation.training.module.resource.dto.ResourceResponse;

import java.util.List;

public interface ResourceService {

    ResourceResponse create(Long userId, CreateResourceRequest request);

    List<ResourceResponse> list(String county, String resourceType, String subject, String grade);

    List<ResourceResponse> listPendingAudit();

    ResourceResponse like(Long resourceId);

    ResourceResponse favorite(Long userId, Long resourceId);

    ResourceResponse watched(Long userId, Long resourceId);

    ResourceResponse review(Long resourceId, String auditStatus);

    CommentResponse comment(Long userId, Long resourceId, CreateCommentRequest request);

    List<CommentResponse> listComments(Long resourceId);
}
