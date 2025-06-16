package com.mchs.mental_health_system.application.controller.auth;

import com.mchs.mental_health_system.application.dto.auth.AuthRequestDTO;
import com.mchs.mental_health_system.application.dto.auth.AuthResponseDTO;
import com.mchs.mental_health_system.application.services.config.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO requestDTO) {
        AuthResponseDTO response = authenticationService.login(requestDTO);
        return ResponseEntity.ok(response);
    }
}