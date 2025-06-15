package com.mchs.mental_health_system.application.factory.user;
import com.mchs.mental_health_system.application.dto.user.UserCreationRequestDTO;
import com.mchs.mental_health_system.application.mappers.user.UserMapper;
import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import com.mchs.mental_health_system.domain.model.entities.user.SystemUser;
import com.mchs.mental_health_system.domain.model.enums.userManagement.AccessProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HealthProfessionalFactory implements UserFactory {

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    @Override
    public AccessProfile getSupportedProfile() {
        return AccessProfile.ROLE_CLINICAL;
    }

    @Override
    public SystemUser create(UserCreationRequestDTO dto) {
        HealthProfessional professional = userMapper.toHealthProfessional(dto);

        professional.setPasswordHash(passwordEncoder.encode(dto.password()));

        professional.setProfile(this.getSupportedProfile());

        return professional;
    }
}