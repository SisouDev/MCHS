package com.mchs.mental_health_system.application.controller.facility;

import com.mchs.mental_health_system.application.dto.facility.CareFacilityCreationDTO;
import com.mchs.mental_health_system.application.dto.facility.CareFacilityResponseDTO;
import com.mchs.mental_health_system.application.services.facility.CareFacilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/facilities")
@RequiredArgsConstructor
public class CareFacilityController {

    private final CareFacilityService careFacilityService;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<CareFacilityResponseDTO> registerFacility(@Valid @RequestBody CareFacilityCreationDTO creationDTO) {
        CareFacilityResponseDTO newFacility = careFacilityService.registerFacility(creationDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newFacility.id()).toUri();
        return ResponseEntity.created(location).body(newFacility);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CareFacilityResponseDTO> findById(@PathVariable Long id) {
        CareFacilityResponseDTO facility = careFacilityService.findById(id);
        return ResponseEntity.ok(facility);
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CareFacilityResponseDTO>> searchFacilities(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city) {
        List<CareFacilityResponseDTO> facilities = careFacilityService.searchFacilities(name, city);
        return ResponseEntity.ok(facilities);
    }
}