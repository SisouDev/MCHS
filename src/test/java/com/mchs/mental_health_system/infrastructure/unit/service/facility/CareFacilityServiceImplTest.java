package com.mchs.mental_health_system.infrastructure.unit.service.facility;

import com.mchs.mental_health_system.application.dto.facility.CareFacilityCreationDTO;
import com.mchs.mental_health_system.application.dto.facility.CareFacilityResponseDTO;
import com.mchs.mental_health_system.application.mappers.facility.CareFacilityMapper;
import com.mchs.mental_health_system.domain.model.entities.facility.CareFacility;
import com.mchs.mental_health_system.domain.model.enums.others.Country;
import com.mchs.mental_health_system.domain.model.shared.embeddable.Address;
import com.mchs.mental_health_system.domain.repositories.facility.CareFacilityRepository;
import com.mchs.mental_health_system.exceptions.common.BusinessException;
import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;
import com.mchs.mental_health_system.infrastructure.services.facility.CareFacilityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CareFacilityServiceImplTest {

    @Mock
    private CareFacilityRepository careFacilityRepository;
    @Mock
    private CareFacilityMapper careFacilityMapper;
    @InjectMocks
    private CareFacilityServiceImpl careFacilityService;

    private CareFacility careFacility;
    private CareFacilityCreationDTO creationDTO;
    private CareFacilityResponseDTO responseDTO;
    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address(
                "Rua da Glória, 123",
                "Belo Horizonte",
                "MG",
                "37310-000",
                Country.BRAZIL
        );

        careFacility = new CareFacility();
        careFacility.setId(1L);
        careFacility.setName("Clínica de Saúde Mental Bem-Estar");
        careFacility.setAddress(address);

        creationDTO = new CareFacilityCreationDTO("Clínica de Saúde Mental Bem-Estar", address, "contato@bemestar.com", "(32) 99999-1234");
        responseDTO = new CareFacilityResponseDTO(1L, "Clínica de Saúde Mental Bem-Estar", address, "(32) 99999-1234", "contato@bemestar.com", 0, 0, 0);
    }

    @Test
    @DisplayName("registerFacility deve registrar uma nova unidade com sucesso")
    void registerFacility_shouldRegisterSuccessfully() {
        when(careFacilityRepository.findByName(creationDTO.name())).thenReturn(Optional.empty());
        when(careFacilityMapper.toEntity(creationDTO)).thenReturn(careFacility);
        when(careFacilityRepository.save(careFacility)).thenReturn(careFacility);
        when(careFacilityMapper.toResponseDTO(careFacility)).thenReturn(responseDTO);

        CareFacilityResponseDTO result = careFacilityService.registerFacility(creationDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(careFacilityRepository).save(careFacility);
    }

    @Test
    @DisplayName("registerFacility deve lançar exceção se o nome já existir")
    void registerFacility_shouldThrowException_whenNameAlreadyExists() {
        when(careFacilityRepository.findByName(creationDTO.name())).thenReturn(Optional.of(careFacility));

        assertThatThrownBy(() -> careFacilityService.registerFacility(creationDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("A Care Facility with the name '" + creationDTO.name() + "' already exists.");

        verify(careFacilityRepository, never()).save(any());
    }

    @Test
    @DisplayName("findById deve retornar uma unidade existente")
    void findById_shouldReturnFacility() {
        when(careFacilityRepository.findById(1L)).thenReturn(Optional.of(careFacility));
        when(careFacilityMapper.toResponseDTO(careFacility)).thenReturn(responseDTO);

        CareFacilityResponseDTO result = careFacilityService.findById(1L);

        assertThat(result).isEqualTo(responseDTO);
    }

    @Test
    @DisplayName("findById deve lançar exceção se a unidade não for encontrada")
    void findById_shouldThrowException_whenNotFound() {
        when(careFacilityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> careFacilityService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("searchFacilities deve buscar por nome e cidade")
    void searchFacilities_shouldSearchByNameAndCity() {
        when(careFacilityRepository.findByNameContainingIgnoreCaseAndAddressCityIgnoreCase("Bem-Estar", "Belo Horizonte")).thenReturn(List.of(careFacility));

        careFacilityService.searchFacilities("Bem-Estar", "Belo Horizonte");

        verify(careFacilityRepository).findByNameContainingIgnoreCaseAndAddressCityIgnoreCase("Bem-Estar", "Belo Horizonte");
        verify(careFacilityMapper).toResponseDTO(careFacility);
    }

    @Test
    @DisplayName("searchFacilities deve buscar apenas por nome")
    void searchFacilities_shouldSearchByNameOnly() {
        when(careFacilityRepository.findByNameContainingIgnoreCase("Bem-Estar")).thenReturn(List.of(careFacility));

        careFacilityService.searchFacilities("Bem-Estar", "");

        verify(careFacilityRepository).findByNameContainingIgnoreCase("Bem-Estar");
        verify(careFacilityRepository, never()).findByAddressCityIgnoreCase(anyString());
    }

    @Test
    @DisplayName("searchFacilities deve buscar apenas por cidade")
    void searchFacilities_shouldSearchByCityOnly() {
        when(careFacilityRepository.findByAddressCityIgnoreCase("Belo Horizonte")).thenReturn(List.of(careFacility));

        careFacilityService.searchFacilities(" ", "Belo Horizonte");

        verify(careFacilityRepository).findByAddressCityIgnoreCase("Belo Horizonte");
        verify(careFacilityRepository, never()).findByNameContainingIgnoreCase(anyString());
    }

    @Test
    @DisplayName("searchFacilities deve retornar lista vazia se não houver parâmetros")
    void searchFacilities_shouldReturnEmptyList_whenNoParameters() {
        List<CareFacilityResponseDTO> result = careFacilityService.searchFacilities(" ", null);

        assertThat(result).isEmpty();
        verify(careFacilityRepository, never()).findByNameContainingIgnoreCaseAndAddressCityIgnoreCase(anyString(), anyString());
        verify(careFacilityRepository, never()).findByNameContainingIgnoreCase(anyString());
        verify(careFacilityRepository, never()).findByAddressCityIgnoreCase(anyString());
    }
}