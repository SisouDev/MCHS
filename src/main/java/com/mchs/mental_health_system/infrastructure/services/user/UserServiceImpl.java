package com.mchs.mental_health_system.infrastructure.services.user;

import com.mchs.mental_health_system.application.dto.user.AdministrativeProfessionalResponseDTO;
import com.mchs.mental_health_system.application.dto.user.HealthProfessionalResponseDTO;
import com.mchs.mental_health_system.application.dto.user.UserCreationRequestDTO;
import com.mchs.mental_health_system.application.mappers.user.UserMapper;
import com.mchs.mental_health_system.application.services.user.UserService;
import com.mchs.mental_health_system.domain.model.entities.user.AdministrativeProfessional;
import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import com.mchs.mental_health_system.domain.model.enums.userManagement.AccessProfile;
import com.mchs.mental_health_system.domain.model.enums.userManagement.AdministrativeRole;
import com.mchs.mental_health_system.domain.model.enums.userManagement.MedicalSpecialty;
import com.mchs.mental_health_system.domain.repositories.user.AdministrativeProfessionalRepository;
import com.mchs.mental_health_system.domain.repositories.user.HealthProfessionalRepository;
import com.mchs.mental_health_system.domain.repositories.user.SystemUserRepository;
import com.mchs.mental_health_system.exceptions.user.UserRoleMismatchException;
import com.mchs.mental_health_system.exceptions.user.UsernameAlreadyExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final SystemUserRepository userRepository;
    private final HealthProfessionalRepository healthProfessionalRepository;
    private final AdministrativeProfessionalRepository administrativeProfessionalRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public HealthProfessionalResponseDTO createHealthProfessional(UserCreationRequestDTO creationDTO) {
        validateUsername(creationDTO.username());
        if(creationDTO.profile() != AccessProfile.ROLE_CLINICAL){
            throw new UserRoleMismatchException("Invalid profile for Health Professional. Expected ROLE_CLINICAL.");
        }
        HealthProfessional newProfessional = userMapper.toHealthProfessional(creationDTO);
        newProfessional.setPasswordHash(passwordEncoder.encode(creationDTO.password()));
        HealthProfessional savedProfessional = healthProfessionalRepository.save(newProfessional);
        return userMapper.toHealthProfessionalResponseDTO(savedProfessional);
    }

    @Override
    @Transactional
    public AdministrativeProfessionalResponseDTO createAdministrativeProfessional(UserCreationRequestDTO creationDTO) {
        validateUsername(creationDTO.username());
        if(creationDTO.profile() != AccessProfile.ROLE_ADMINISTRATIVE){
            throw new UserRoleMismatchException("Invalid profile for Administrative Professional. Expected ROLE_ADMINISTRATIVE.");
        }
        AdministrativeProfessional newProfessional = userMapper.toAdministrativeProfessional(creationDTO);
        newProfessional.setPasswordHash(passwordEncoder.encode(creationDTO.password()));
        AdministrativeProfessional savedProfessional = administrativeProfessionalRepository.save(newProfessional);
        return userMapper.toAdministrativeProfessionalResponseDTO(savedProfessional);
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

    private void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username);
        }
    }
}
