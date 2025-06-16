package com.mchs.mental_health_system.infrastructure.services.user;

import com.mchs.mental_health_system.application.dto.user.AdministrativeProfessionalResponseDTO;
import com.mchs.mental_health_system.application.dto.user.HealthProfessionalResponseDTO;
import com.mchs.mental_health_system.application.dto.user.UserCreationRequestDTO;
import com.mchs.mental_health_system.application.factory.user.UserFactory;
import com.mchs.mental_health_system.application.mappers.user.UserMapper;
import com.mchs.mental_health_system.application.services.user.UserService;
import com.mchs.mental_health_system.domain.model.entities.user.AdministrativeProfessional;
import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import com.mchs.mental_health_system.domain.model.entities.user.SystemUser;
import com.mchs.mental_health_system.domain.model.enums.userManagement.AccessProfile;
import com.mchs.mental_health_system.domain.model.enums.userManagement.AdministrativeRole;
import com.mchs.mental_health_system.domain.model.enums.userManagement.MedicalSpecialty;
import com.mchs.mental_health_system.domain.repositories.user.AdministrativeProfessionalRepository;
import com.mchs.mental_health_system.domain.repositories.user.HealthProfessionalRepository;
import com.mchs.mental_health_system.domain.repositories.user.SystemUserRepository;
import com.mchs.mental_health_system.exceptions.common.BusinessException;
import com.mchs.mental_health_system.exceptions.user.UsernameAlreadyExistsException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final List<UserFactory> userFactories;
    private final SystemUserRepository systemUserRepository;
    private final HealthProfessionalRepository healthProfessionalRepository;
    private final AdministrativeProfessionalRepository administrativeProfessionalRepository;
    private final UserMapper userMapper;

    private final Map<AccessProfile, UserFactory> factoryMap = new EnumMap<>(AccessProfile.class);

    @PostConstruct
    public void init() {
        for (UserFactory factory : userFactories) {
            factoryMap.put(factory.getSupportedProfile(), factory);
        }
    }

    @Override
    public HealthProfessionalResponseDTO createHealthProfessional(UserCreationRequestDTO creationDTO) {
        SystemUser user = create(creationDTO);
        return userMapper.toHealthProfessionalResponseDTO((HealthProfessional) user);
    }

    @Override
    public AdministrativeProfessionalResponseDTO createAdministrativeProfessional(UserCreationRequestDTO creationDTO) {
        SystemUser user = create(creationDTO);
        return userMapper.toAdministrativeProfessionalResponseDTO((AdministrativeProfessional) user);
    }

    private SystemUser create(UserCreationRequestDTO dto) {
        if (systemUserRepository.existsByUsername(dto.username())) {
            throw new UsernameAlreadyExistsException(dto.username());
        }

        UserFactory factory = Optional.ofNullable(factoryMap.get(dto.profile()))
                .orElseThrow(() -> new BusinessException("No factory found for profile: " + dto.profile()));

        SystemUser user = factory.create(dto);
        return systemUserRepository.save(user);
    }

    @Override
    public List<HealthProfessionalResponseDTO> findHealthProfessionalsBySpecialty(MedicalSpecialty specialty) {
        return healthProfessionalRepository.findBySpecialty(specialty).stream()
                .map(userMapper::toHealthProfessionalResponseDTO)
                .toList();
    }

    @Override
    public List<AdministrativeProfessionalResponseDTO> findAdministrativeProfessionalsByRole(AdministrativeRole role) {
        return administrativeProfessionalRepository.findByRole(role).stream()
                .map(userMapper::toAdministrativeProfessionalResponseDTO)
                .toList();
    }
}
