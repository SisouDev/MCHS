package com.mchs.mental_health_system.infrastructure.services.config;

import com.mchs.mental_health_system.application.dto.auth.AuthRequestDTO;
import com.mchs.mental_health_system.application.dto.auth.AuthResponseDTO;
import com.mchs.mental_health_system.application.services.config.AuthenticationService;
import com.mchs.mental_health_system.domain.model.entities.user.SystemUser;
import com.mchs.mental_health_system.domain.model.shared.functions.Auditable;
import com.mchs.mental_health_system.domain.repositories.user.SystemUserRepository;
import com.mchs.mental_health_system.exceptions.user.UserNotFoundException;
import com.mchs.mental_health_system.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final SystemUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Auditable(action = "USER_LOGIN_SUCCESS")
    public AuthResponseDTO login(AuthRequestDTO requestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDTO.username(), requestDTO.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);

        return new AuthResponseDTO(requestDTO.username(), jwt);
    }

    @Override
    @Transactional
    @Auditable(action = "USER_CREATED")
    public void changePassword(String username, String oldPassword, String newPassword) {
        SystemUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new org.springframework.security.authentication.BadCredentialsException("Invalid old password.");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            String token = UUID.randomUUID().toString();
        });
    }
}
