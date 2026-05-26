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
        if (existsByUsername(username)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(StringUtils.hasText(request.getNickname()) ? request.getNickname().trim() : username);
        user.setRole(DEFAULT_ROLE);
        user.setStatus(STATUS_ENABLED);
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        save(user);

        return UserResponse.from(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = getUserByUsername(request.getUsername().trim());
        if (user.getStatus() == null || user.getStatus() == 0) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "用户已被禁用");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户名或密码错误");
        }

        String token = jwtTokenProvider.generateToken(user.getUsername());
        return new LoginResponse(token, "Bearer", UserResponse.from(user));
    }

    @Override
    public UserResponse getByUsername(String username) {
        return UserResponse.from(getUserByUsername(username));
    }

    private boolean existsByUsername(String username) {
        return count(new LambdaQueryWrapper<User>().eq(User::getUsername, username)) > 0;
    }

    private User getUserByUsername(String username) {
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .last("LIMIT 1"));
        if (user == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户名或密码错误");
        }
        return user;
    }
}
