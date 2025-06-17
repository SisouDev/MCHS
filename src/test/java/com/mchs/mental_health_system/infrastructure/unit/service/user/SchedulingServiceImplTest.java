package com.mchs.mental_health_system.infrastructure.unit.service.user;

import com.mchs.mental_health_system.application.dto.clinicalRecords.ConsultationRequestDTO;
import com.mchs.mental_health_system.application.services.clinicalRecords.ConsultationSessionService;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.ConsultationSession;
import com.mchs.mental_health_system.domain.model.entities.user.Availability;
import com.mchs.mental_health_system.domain.repositories.clinicalRecords.ConsultationSessionRepository;
import com.mchs.mental_health_system.domain.repositories.user.AvailabilityRepository;
import com.mchs.mental_health_system.exceptions.common.BusinessException;
import com.mchs.mental_health_system.infrastructure.services.user.SchedulingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchedulingServiceImplTest {

    @Mock
    private AvailabilityRepository availabilityRepository;
    @Mock
    private ConsultationSessionRepository consultationSessionRepository;
    @Mock
    private ConsultationSessionService consultationSessionService;

    @InjectMocks
    private SchedulingServiceImpl schedulingService;

    @Spy
    @InjectMocks
    private SchedulingServiceImpl schedulingServiceSpy;


    @Test
    @DisplayName("findAvailableSlots deve retornar horários livres quando não há agendamentos")
    void findAvailableSlots_shouldReturnAllSlots_whenNoAppointmentsExist() {
        Long professionalId = 1L;
        LocalDate date = LocalDate.of(2025, 6, 17);
        DayOfWeek day = date.getDayOfWeek();

        Availability availability = new Availability();
        availability.setStartTime(LocalTime.of(9, 0));
        availability.setEndTime(LocalTime.of(12, 0));

        when(availabilityRepository.findByHealthProfessionalIdAndDayOfWeek(professionalId, day)).thenReturn(List.of(availability));
        when(consultationSessionRepository.findByHealthProfessionalIdAndSessionDateTimeBetween(anyLong(), any(), any())).thenReturn(Collections.emptyList());

        List<LocalDateTime> slots = schedulingService.findAvailableSlots(professionalId, date);

        assertThat(slots).hasSize(3);
        assertThat(slots).containsExactly(
                date.atTime(9, 0),
                date.atTime(10, 0),
                date.atTime(11, 0)
        );
    }

    @Test
    @DisplayName("findAvailableSlots deve retornar apenas horários não conflitantes")
    void findAvailableSlots_shouldReturnOnlyNonConflictingSlots() {
        // Arrange
        Long professionalId = 1L;
        LocalDate date = LocalDate.of(2025, 6, 17);
        DayOfWeek day = date.getDayOfWeek();

        Availability availability = new Availability();
        availability.setStartTime(LocalTime.of(9, 0));
        availability.setEndTime(LocalTime.of(12, 0));

        ConsultationSession existingSession = new ConsultationSession();
        existingSession.setSessionDateTime(date.atTime(10, 0));

        when(availabilityRepository.findByHealthProfessionalIdAndDayOfWeek(professionalId, day)).thenReturn(List.of(availability));
        when(consultationSessionRepository.findByHealthProfessionalIdAndSessionDateTimeBetween(anyLong(), any(), any())).thenReturn(List.of(existingSession));

        List<LocalDateTime> slots = schedulingService.findAvailableSlots(professionalId, date);

        assertThat(slots).hasSize(2);
        assertThat(slots).containsExactly(
                date.atTime(9, 0),
                date.atTime(11, 0)
        );
    }

    @Test
    @DisplayName("bookAppointment deve agendar com sucesso um horário disponível")
    void bookAppointment_shouldSucceed_whenSlotIsAvailable() {
        LocalDateTime desiredSlot = LocalDateTime.of(2025, 6, 17, 10, 0);
        ConsultationRequestDTO requestDTO = new ConsultationRequestDTO(1L, 1L, desiredSlot, "");

        doReturn(List.of(desiredSlot)).when(schedulingServiceSpy).findAvailableSlots(1L, desiredSlot.toLocalDate());

        schedulingServiceSpy.bookAppointment(requestDTO);

        verify(consultationSessionService).scheduleSession(requestDTO);
    }

    @Test
    @DisplayName("bookAppointment deve lançar exceção para horário indisponível")
    void bookAppointment_shouldFail_whenSlotIsNotAvailable() {
        LocalDateTime desiredSlot = LocalDateTime.of(2025, 6, 17, 10, 0);
        ConsultationRequestDTO requestDTO = new ConsultationRequestDTO(1L, 1L, desiredSlot, "");

        doReturn(Collections.emptyList()).when(schedulingServiceSpy).findAvailableSlots(1L, desiredSlot.toLocalDate());

        assertThatThrownBy(() -> schedulingServiceSpy.bookAppointment(requestDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("The requested time slot is not available.");

        verify(consultationSessionService, never()).scheduleSession(any());
    }
}