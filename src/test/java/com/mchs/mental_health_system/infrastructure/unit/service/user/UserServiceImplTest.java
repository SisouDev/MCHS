package com.mchs.mental_health_system.infrastructure.unit.service.user;

import com.mchs.mental_health_system.application.dto.user.UserCreationRequestDTO;
import com.mchs.mental_health_system.application.factory.user.UserFactory;
import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import com.mchs.mental_health_system.domain.model.entities.user.SystemUser;
import com.mchs.mental_health_system.domain.model.enums.userManagement.AccessProfile;
import com.mchs.mental_health_system.domain.model.enums.userManagement.MedicalSpecialty;
import com.mchs.mental_health_system.domain.repositories.user.SystemUserRepository;
import com.mchs.mental_health_system.exceptions.common.BusinessException;
import com.mchs.mental_health_system.exceptions.user.UsernameAlreadyExistsException;
import com.mchs.mental_health_system.infrastructure.services.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private SystemUserRepository systemUserRepository;

    @Spy
    private List<UserFactory> userFactories = new ArrayList<>();

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserFactory healthProfessionalFactory;

    @Mock
    private UserFactory administrativeFactory;

    private UserCreationRequestDTO healthProDTO;

    @BeforeEach
    void setUp() {
        when(healthProfessionalFactory.getSupportedProfile()).thenReturn(AccessProfile.ROLE_CLINICAL);
        when(administrativeFactory.getSupportedProfile()).thenReturn(AccessProfile.ROLE_ADMINISTRATIVE);
        userFactories.add(healthProfessionalFactory);
        userFactories.add(administrativeFactory);
        userService.init();
        healthProDTO = new UserCreationRequestDTO("dr.joao", "joao@email.com", "password123", AccessProfile.ROLE_CLINICAL, null, MedicalSpecialty.PSYCHIATRIST, "CRM-12345", null, 1L);
    }

    @Test
    @DisplayName("createUser deve chamar a fábrica correta para o perfil HEALTH_PROFESSIONAL")
    void createUser_shouldCallCorrectFactory_forHealthProfessional() {
        when(systemUserRepository.existsByUsername(healthProDTO.username())).thenReturn(false);
        when(healthProfessionalFactory.create(healthProDTO)).thenReturn(new HealthProfessional());

        userService.createUser(healthProDTO);

        verify(healthProfessionalFactory).create(healthProDTO);
        verify(administrativeFactory, never()).create(any());
        verify(systemUserRepository).save(any(SystemUser.class));
    }

    @Test
    @DisplayName("createUser deve lançar UsernameAlreadyExistsException se o username já existir")
    void createUser_shouldThrowException_whenUsernameExists() {
        when(systemUserRepository.existsByUsername(healthProDTO.username())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(healthProDTO))
                .isInstanceOf(UsernameAlreadyExistsException.class);

        verify(systemUserRepository, never()).save(any());
    }

    @Test
    @DisplayName("createUser deve lançar BusinessException se nenhuma fábrica for encontrada para o perfil")
    void createUser_shouldThrowException_whenNoFactoryFound() {
        UserCreationRequestDTO dtoWithUnknownProfile = new UserCreationRequestDTO("test", "test@email.com", "password123", null, null, null, null, null, 1L);

        when(systemUserRepository.existsByUsername(dtoWithUnknownProfile.username())).thenReturn(false);

        assertThatThrownBy(() -> userService.createUser(dtoWithUnknownProfile))
                .isInstanceOf(BusinessException.class)
                .hasMessage("No factory found for profile: null");
    }
}