package com.innovation.training.module.user.service;

import com.innovation.training.module.user.dto.LoginRequest;
import com.innovation.training.module.user.dto.LoginResponse;
import com.innovation.training.module.user.dto.RegisterRequest;
import com.innovation.training.module.user.dto.UserResponse;

public interface UserService {

    UserResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    UserResponse getByUsername(String username);
}
