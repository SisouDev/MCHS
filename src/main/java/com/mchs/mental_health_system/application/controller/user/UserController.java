package com.mchs.mental_health_system.application.controller.user;

import com.mchs.mental_health_system.application.dto.user.AdministrativeProfessionalResponseDTO;
import com.mchs.mental_health_system.application.dto.user.HealthProfessionalResponseDTO;
import com.mchs.mental_health_system.application.dto.user.UserCreationRequestDTO;
import com.mchs.mental_health_system.application.mappers.user.UserMapper;
import com.mchs.mental_health_system.application.services.user.UserService;
import com.mchs.mental_health_system.domain.model.entities.user.AdministrativeProfessional;
import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import com.mchs.mental_health_system.domain.model.entities.user.SystemUser;
import com.mchs.mental_health_system.domain.model.enums.userManagement.AdministrativeRole;
import com.mchs.mental_health_system.domain.model.enums.userManagement.MedicalSpecialty;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATIVE', 'MANAGER')")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserCreationRequestDTO creationDTO) {
        SystemUser createdUser = userService.createUser(creationDTO);


        if (createdUser instanceof HealthProfessional) {
            HealthProfessionalResponseDTO response = userMapper.toHealthProfessionalResponseDTO((HealthProfessional) createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else if (createdUser instanceof AdministrativeProfessional) {
            AdministrativeProfessionalResponseDTO response = userMapper.toAdministrativeProfessionalResponseDTO((AdministrativeProfessional) createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/health-professionals")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<HealthProfessionalResponseDTO>> findHealthProfessionalsBySpecialty(@RequestParam MedicalSpecialty specialty) {
        List<HealthProfessionalResponseDTO> professionals = userService.findHealthProfessionalsBySpecialty(specialty);
        return ResponseEntity.ok(professionals);
    }

    @GetMapping("/administrative-professionals")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AdministrativeProfessionalResponseDTO>> findAdministrativeProfessionalsByRole(@RequestParam AdministrativeRole role) {
        List<AdministrativeProfessionalResponseDTO> professionals = userService.findAdministrativeProfessionalsByRole(role);
        return ResponseEntity.ok(professionals);
    }
}