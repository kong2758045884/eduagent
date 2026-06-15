package com.innovation.training.support;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.innovation.training.common.BusinessException;
import com.innovation.training.common.ErrorCode;
import com.innovation.training.module.user.entity.User;
import com.innovation.training.module.user.mapper.UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Set;

@Component
public class CurrentUserService {

    private static final String TEACHER_TYPE_HEADER = "X-Teacher-Type";
    private static final String TEACHER_TYPE_AUTHORITY_PREFIX = "TEACHER_TYPE_";
    private static final Set<String> VALID_TEACHER_TYPES = Set.of("senior", "mid", "novice");

    private final UserMapper userMapper;

    public CurrentUserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User requireUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        String teacherType = resolveTeacherType(authentication);
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, authentication.getName())
                .eq(StringUtils.hasText(teacherType), User::getTeacherType, teacherType)
                .orderByAsc(User::getId)
                .last("LIMIT 1"));
        if (user == null && StringUtils.hasText(teacherType)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "当前账号未注册该教师身份");
        }
        if (user == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        return user;
    }

    public Long requireUserId(Authentication authentication) {
        return requireUser(authentication).getId();
    }

    private String resolveTeacherType(Authentication authentication) {
        String headerTeacherType = requestTeacherType();
        if (isValidTeacherType(headerTeacherType)) {
            return headerTeacherType;
        }

        if (authentication.getAuthorities() == null) {
            return null;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith(TEACHER_TYPE_AUTHORITY_PREFIX))
                .map(authority -> authority.substring(TEACHER_TYPE_AUTHORITY_PREFIX.length()))
                .filter(this::isValidTeacherType)
                .findFirst()
                .orElse(null);
    }

    private String requestTeacherType() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            return null;
        }
        String value = attributes.getRequest().getHeader(TEACHER_TYPE_HEADER);
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private boolean isValidTeacherType(String teacherType) {
        return StringUtils.hasText(teacherType) && VALID_TEACHER_TYPES.contains(teacherType);
    }
}
