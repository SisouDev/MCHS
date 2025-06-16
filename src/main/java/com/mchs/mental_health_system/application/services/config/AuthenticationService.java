package com.mchs.mental_health_system.application.services.config;

import com.mchs.mental_health_system.application.dto.auth.AuthRequestDTO;
import com.mchs.mental_health_system.application.dto.auth.AuthResponseDTO;

public interface AuthenticationService {
    AuthResponseDTO login(AuthRequestDTO requestDTO);
    void changePassword(String username, String oldPassword, String newPassword);
    void requestPasswordReset(String email);
}