package com.mchs.mental_health_system.application.factory.facility;

import com.mchs.mental_health_system.application.dto.facility.CareFacilityCreationDTO;
import com.mchs.mental_health_system.application.mappers.facility.CareFacilityMapper;
import com.mchs.mental_health_system.domain.model.entities.facility.CareFacility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CareFacilityFactory {

    private final CareFacilityMapper careFacilityMapper;

    public CareFacility create(CareFacilityCreationDTO dto) {
        return careFacilityMapper.toEntity(dto);
    }
}