package com.innovation.training.support;

import com.innovation.training.common.BusinessException;
import com.innovation.training.common.ErrorCode;
import com.innovation.training.module.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TeacherAccessService {

    private final CurrentUserService currentUserService;

    public TeacherAccessService(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    public User requireAny(Authentication authentication, String... teacherTypes) {
        User user = currentUserService.requireUser(authentication);
        if (teacherTypes == null || teacherTypes.length == 0) {
            return user;
        }
        boolean matched = Arrays.stream(teacherTypes).anyMatch(type -> type.equals(user.getTeacherType()));
        if (!matched && !"ADMIN".equals(user.getRole())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "当前教师端无权访问该功能");
        }
        return user;
    }
}
