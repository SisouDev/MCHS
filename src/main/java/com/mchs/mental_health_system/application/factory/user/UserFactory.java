package com.mchs.mental_health_system.application.factory.user;

import com.mchs.mental_health_system.application.dto.user.UserCreationRequestDTO;
import com.mchs.mental_health_system.domain.model.entities.user.SystemUser;
import com.mchs.mental_health_system.domain.model.enums.userManagement.AccessProfile;

public interface UserFactory {
    AccessProfile getSupportedProfile();

    SystemUser create(UserCreationRequestDTO dto);
}
