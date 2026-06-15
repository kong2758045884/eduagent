package com.innovation.training.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.innovation.training.common.BusinessException;
import com.innovation.training.common.ErrorCode;
import com.innovation.training.module.user.dto.LoginRequest;
import com.innovation.training.module.user.dto.LoginResponse;
import com.innovation.training.module.user.dto.RegisterRequest;
import com.innovation.training.module.user.dto.UserResponse;
import com.innovation.training.module.user.entity.User;
import com.innovation.training.module.user.mapper.UserMapper;
import com.innovation.training.module.user.service.UserService;
import com.innovation.training.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final String DEFAULT_ROLE = "USER";
    private static final int STATUS_ENABLED = 1;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserServiceImpl(PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponse register(RegisterRequest request) {
        String username = request.getUsername().trim();
        String teacherType = normalizeTeacherType(request.getTeacherType());
        if (getUserByUsernameAndTeacherType(username, teacherType) != null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "该用户身份已注册");
        }

        List<User> existingUsers = listByUsername(username);
        User baseUser = existingUsers.isEmpty() ? null : existingUsers.get(0);
        if (baseUser != null) {
            if (baseUser.getStatus() == null || baseUser.getStatus() == 0) {
                throw new BusinessException(ErrorCode.FORBIDDEN, "用户已被禁用");
            }
            if (!passwordEncoder.matches(request.getPassword(), baseUser.getPassword())) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "用户名已存在，请使用原账号密码添加新身份");
            }
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(baseUser == null ? passwordEncoder.encode(request.getPassword()) : baseUser.getPassword());
        user.setNickname(defaultText(request.getNickname(), baseUser == null ? username : baseUser.getNickname()));
        user.setRole(baseUser == null ? DEFAULT_ROLE : defaultText(baseUser.getRole(), DEFAULT_ROLE));
        user.setTeacherType(teacherType);
        user.setCounty(defaultText(request.getCounty(), baseUser == null ? null : baseUser.getCounty()));
        user.setSchool(defaultText(request.getSchool(), baseUser == null ? null : baseUser.getSchool()));
        user.setAvatarUrl(baseUser == null ? null : baseUser.getAvatarUrl());
        user.setRealName(baseUser == null ? null : baseUser.getRealName());
        user.setPhone(baseUser == null ? null : baseUser.getPhone());
        user.setSubject(defaultText(request.getSubject(), baseUser == null ? "数学" : baseUser.getSubject()));
        user.setGrade(defaultText(request.getGrade(), baseUser == null ? null : baseUser.getGrade()));
        user.setTitle(baseUser == null ? null : baseUser.getTitle());
        user.setTeachingYears(baseUser == null ? null : baseUser.getTeachingYears());
        user.setBio(baseUser == null ? null : baseUser.getBio());
        user.setExpertiseTags(baseUser == null ? null : baseUser.getExpertiseTags());
        user.setStatus(baseUser == null || baseUser.getStatus() == null ? STATUS_ENABLED : baseUser.getStatus());
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        save(user);

        return UserResponse.from(user, getTeacherTypes(username));
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        String username = request.getUsername().trim();
        String teacherType = normalizeTeacherTypeOrNull(request.getTeacherType());
        List<User> users = listByUsername(username);
        if (users.isEmpty()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户名或密码错误");
        }

        User user = teacherType == null
                ? users.get(0)
                : users.stream()
                .filter(item -> teacherType.equals(item.getTeacherType()))
                .findFirst()
                .orElse(null);
        if (user == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "该账号未注册当前身份");
        }
        if (user.getStatus() == null || user.getStatus() == 0) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "用户已被禁用");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户名或密码错误");
        }

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getTeacherType());
        return new LoginResponse(token, "Bearer", UserResponse.from(user, getTeacherTypes(username)));
    }

    @Override
    public UserResponse getByUsername(String username) {
        User user = getPrimaryUserByUsername(username);
        return UserResponse.from(user, getTeacherTypes(username));
    }

    @Override
    public List<String> getTeacherTypes(String username) {
        return listByUsername(username).stream()
                .map(User::getTeacherType)
                .filter(StringUtils::hasText)
                .distinct()
                .toList();
    }

    private User getPrimaryUserByUsername(String username) {
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .orderByAsc(User::getId)
                .last("LIMIT 1"));
        if (user == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户名或密码错误");
        }
        return user;
    }

    private User getUserByUsernameAndTeacherType(String username, String teacherType) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getTeacherType, teacherType)
                .last("LIMIT 1"));
    }

    private List<User> listByUsername(String username) {
        return list(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .orderByAsc(User::getId));
    }

    private String normalizeTeacherType(String teacherType) {
        if (!StringUtils.hasText(teacherType)) {
            return "senior";
        }
        String value = teacherType.trim();
        if ("mid".equals(value) || "novice".equals(value) || "senior".equals(value)) {
            return value;
        }
        return "senior";
    }

    private String normalizeTeacherTypeOrNull(String teacherType) {
        if (!StringUtils.hasText(teacherType)) {
            return null;
        }
        return normalizeTeacherType(teacherType);
    }

    private String defaultText(String value, String fallback) {
        return StringUtils.hasText(value) ? value.trim() : fallback;
    }
}
