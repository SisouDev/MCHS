package com.mchs.mental_health_system.infrastructure.unit.service.auth;

import com.mchs.mental_health_system.application.dto.auth.AuthRequestDTO;
import com.mchs.mental_health_system.application.dto.auth.AuthResponseDTO;
import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import com.mchs.mental_health_system.domain.repositories.user.SystemUserRepository;
import com.mchs.mental_health_system.infrastructure.services.config.AuthenticationServiceImpl;
import com.mchs.mental_health_system.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private SystemUserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private HealthProfessional user;

    @BeforeEach
    void setUp() {
        user = new HealthProfessional();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@email.com");
        user.setPasswordHash("hashedOldPassword");
    }

    @Test
    @DisplayName("login deve retornar DTO de autenticação em caso de sucesso")
    void login_shouldReturnAuthResponseDTO_onSuccess() {
        AuthRequestDTO request = new AuthRequestDTO("testuser", "password");
        Authentication authMock = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authMock);
        when(jwtTokenProvider.generateToken(authMock)).thenReturn("dummy.jwt.token");

        AuthResponseDTO response = authenticationService.login(request);

        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo("testuser");
        assertThat(response.token()).isEqualTo("dummy.jwt.token");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateToken(authMock);
    }

    @Test
    @DisplayName("changePassword deve alterar a senha com sucesso")
    void changePassword_shouldChangePasswordSuccessfully() {
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, "hashedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("hashedNewPassword");

        authenticationService.changePassword("testuser", oldPassword, newPassword);

        verify(passwordEncoder).matches(oldPassword, "hashedOldPassword");
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(user);
        assertThat(user.getPasswordHash()).isEqualTo("hashedNewPassword");
    }

    @Test
    @DisplayName("changePassword deve lançar exceção se a senha antiga for inválida")
    void changePassword_shouldThrowException_whenOldPasswordIsInvalid() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongOldPassword", "hashedOldPassword")).thenReturn(false);

        assertThatThrownBy(() -> authenticationService.changePassword("testuser", "wrongOldPassword", "newPassword"))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Invalid old password.");

        verify(userRepository, never()).save(any());
    }
}