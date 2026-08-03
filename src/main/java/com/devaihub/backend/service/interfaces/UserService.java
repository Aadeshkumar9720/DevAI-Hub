package com.devaihub.backend.service.interfaces;
import com.devaihub.backend.dto.RegisterRequest;
import com.devaihub.backend.entity.User;

import com.devaihub.backend.dto.LoginRequest;
import com.devaihub.backend.response.LoginResponse;
public interface UserService {
    User registerUser(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
