package com.mchs.mental_health_system.domain.repositories.user;

import com.mchs.mental_health_system.domain.model.entities.user.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {
    Optional<SystemUser> findByUsername(String username);
}