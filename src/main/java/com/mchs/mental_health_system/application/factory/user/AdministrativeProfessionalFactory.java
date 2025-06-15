package com.mchs.mental_health_system.application.factory.user;
import com.mchs.mental_health_system.application.dto.user.UserCreationRequestDTO;
import com.mchs.mental_health_system.application.mappers.user.UserMapper;
import com.mchs.mental_health_system.domain.model.entities.user.AdministrativeProfessional;
import com.mchs.mental_health_system.domain.model.entities.user.SystemUser;
import com.mchs.mental_health_system.domain.model.enums.userManagement.AccessProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdministrativeProfessionalFactory implements UserFactory {

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public AccessProfile getSupportedProfile() {
        return AccessProfile.ROLE_ADMINISTRATIVE;
    }

    @Override
    public SystemUser create(UserCreationRequestDTO dto) {
        AdministrativeProfessional admin = userMapper.toAdministrativeProfessional(dto);

        admin.setPasswordHash(passwordEncoder.encode(dto.password()));
        admin.setProfile(this.getSupportedProfile());

        return admin;
    }
}