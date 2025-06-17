package com.mchs.mental_health_system.application.factory.user;

import com.mchs.mental_health_system.application.dto.user.UserCreationRequestDTO;
import com.mchs.mental_health_system.application.mappers.user.UserMapper;
import com.mchs.mental_health_system.domain.model.entities.user.Manager;
import com.mchs.mental_health_system.domain.model.entities.user.SystemUser;
import com.mchs.mental_health_system.domain.model.enums.userManagement.AccessProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManagerFactory implements UserFactory {

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public AccessProfile getSupportedProfile() {
        return AccessProfile.ROLE_MANAGER;
    }

    @Override
    public SystemUser create(UserCreationRequestDTO dto) {
        Manager manager = userMapper.toManager(dto);
        manager.setPasswordHash(passwordEncoder.encode(dto.password()));
        manager.setProfile(this.getSupportedProfile());
        return manager;
    }
}