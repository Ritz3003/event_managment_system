package com.ritu.eventplatform.service;

import com.ritu.eventplatform.dto.ChangePasswordRequest;
import com.ritu.eventplatform.dto.ChangePasswordResponse;
import com.ritu.eventplatform.dto.LoginRequest;
import com.ritu.eventplatform.dto.LoginResponse;
import com.ritu.eventplatform.dto.SignupRequest;
import com.ritu.eventplatform.dto.SignupResponse;

public interface AuthService {
         SignupResponse signup(SignupRequest request);
         LoginResponse login(LoginRequest request);
         ChangePasswordResponse changePassword(ChangePasswordRequest request);
}
