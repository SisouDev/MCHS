package com.mchs.mental_health_system.application.services.facility;
import com.mchs.mental_health_system.application.dto.facility.CareFacilityCreationDTO;
import com.mchs.mental_health_system.application.dto.facility.CareFacilityResponseDTO;

import java.util.List;

public interface CareFacilityService {

    CareFacilityResponseDTO registerFacility(CareFacilityCreationDTO creationDTO);

    CareFacilityResponseDTO findById(Long id);

    List<CareFacilityResponseDTO> searchFacilities(String nameFragment, String city);

}