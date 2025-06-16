package com.mchs.mental_health_system.infrastructure.services.facility;

import com.mchs.mental_health_system.application.dto.facility.CareFacilityCreationDTO;
import com.mchs.mental_health_system.application.dto.facility.CareFacilityResponseDTO;
import com.mchs.mental_health_system.application.mappers.facility.CareFacilityMapper;
import com.mchs.mental_health_system.application.services.facility.CareFacilityService;
import com.mchs.mental_health_system.domain.model.entities.facility.CareFacility;
import com.mchs.mental_health_system.domain.repositories.facility.CareFacilityRepository;
import com.mchs.mental_health_system.exceptions.common.BusinessException;
import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CareFacilityServiceImpl implements CareFacilityService {

    private final CareFacilityRepository careFacilityRepository;
    private final CareFacilityMapper careFacilityMapper;

    @Override
    @Transactional
    public CareFacilityResponseDTO registerFacility(CareFacilityCreationDTO creationDTO) {
        careFacilityRepository.findByName(creationDTO.name()).ifPresent(cf -> {
            throw new BusinessException("A Care Facility with the name '" + creationDTO.name() + "' already exists.");
        });

        CareFacility newFacility = careFacilityMapper.toEntity(creationDTO);
        CareFacility savedFacility = careFacilityRepository.save(newFacility);

        return careFacilityMapper.toResponseDTO(savedFacility);
    }

    @Override
    public CareFacilityResponseDTO findById(Long id) {
        CareFacility facility = findFacilityByIdOrThrow(id);
        return careFacilityMapper.toResponseDTO(facility);
    }

    @Override
    public List<CareFacilityResponseDTO> searchFacilities(String nameFragment, String city) {
        boolean hasName = StringUtils.hasText(nameFragment);
        boolean hasCity = StringUtils.hasText(city);

        List<CareFacility> facilities;

        if (hasName && hasCity) {
            facilities = careFacilityRepository.findByNameContainingIgnoreCaseAndAddressCityIgnoreCase(nameFragment, city);
        } else if (hasName) {
            facilities = careFacilityRepository.findByNameContainingIgnoreCase(nameFragment);
        } else if (hasCity) {
            facilities = careFacilityRepository.findByAddressCityIgnoreCase(city);
        } else {
            return Collections.emptyList();
        }

        return facilities.stream()
                .map(careFacilityMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    private CareFacility findFacilityByIdOrThrow(Long id) {
        return careFacilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Care Facility not found with ID: " + id));
    }
}
