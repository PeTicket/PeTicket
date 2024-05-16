package tqs.peticket.client.serviceTests;

import tqs.peticket.client.service.AppointmentService;
import tqs.peticket.client.model.Appointment;
import tqs.peticket.client.repository.AppointmentRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Appointment appointment1;
    private Appointment appointment2;

    @BeforeEach
    void setUp() {
        appointment1 = new Appointment();
        appointment1.setId(UUID.randomUUID());
        appointment1.setPetId(UUID.randomUUID());
        appointment1.setVetId(UUID.randomUUID());
        appointment1.setDate("2024-05-17");
        appointment1.setTime("15:00");
        appointment1.setPrescription("Brufen");
        appointment1.setObservations("Hurt paw");

        appointment2 = new Appointment();
        appointment2.setId(UUID.randomUUID());
        appointment2.setPetId(UUID.randomUUID());
        appointment2.setVetId(UUID.randomUUID());
        appointment2.setDate("2024-05-05");
        appointment2.setTime("10:00");
        appointment2.setPrescription("Benuron");
        appointment2.setObservations("Bleeeding");
    }

    @Test
    @DisplayName("Test save appointment")
    void testSaveAppointment() {
        when(appointmentRepository.save(appointment1)).thenReturn(appointment1);
        assertThat(appointmentService.save(appointment1)).isEqualTo(appointment1);
    }

    @Test
    @DisplayName("Test find appointment by id")
    void testFindAppointmentById() {
        when(appointmentRepository.findById(appointment1.getId())).thenReturn(Optional.of(appointment1).get());
        assertThat(appointmentService.findById(appointment1.getId())).isEqualTo(appointment1);
    }

    @Test
    @DisplayName("Test delete appointment")
    void testDeleteAppointment() {
        when(appointmentRepository.existsById(appointment1.getId())).thenReturn(true);
        appointmentService.delete(appointment1);
        verify(appointmentRepository, times(1)).deleteById(appointment1.getId());
    }

    @Test
    @DisplayName("Test delete appointment by id")
    void testDeleteAppointmentById() {
        when(appointmentRepository.existsById(appointment1.getId())).thenReturn(true);
        appointmentService.deleteById(appointment1.getId());
        verify(appointmentRepository, times(1)).deleteById(appointment1.getId());
    }

    @Test
    @DisplayName("Test exists appointment by id")
    void testExistsAppointmentById() {
        when(appointmentRepository.existsById(appointment1.getId())).thenReturn(true);
        assertThat(appointmentService.existsById(appointment1.getId())).isTrue();
    }

    @Test
    @DisplayName("Test exists appointment by user id")
    void testExistsAppointmentByUserId() {
        when(appointmentRepository.existsByUserId(appointment1.getUserId())).thenReturn(true);
        assertThat(appointmentService.existsByUserId(appointment1.getUserId())).isTrue();
    }

    @Test
    @DisplayName("Test exists appointment by pet id")
    void testExistsAppointmentByPetId() {
        when(appointmentRepository.existsByPetId(appointment1.getPetId())).thenReturn(true);
        assertThat(appointmentService.existsByPetId(appointment1.getPetId())).isTrue();
    }

    @Test
    @DisplayName("Test exists appointment by vet id")
    void testExistsAppointmentByVetId() {
        when(appointmentRepository.existsByVetId(appointment1.getVetId())).thenReturn(true);
        assertThat(appointmentService.existsByVetId(appointment1.getVetId())).isTrue();
    }

    @Test
    @DisplayName("Test update appointment")
    void testUpdateAppointment() {
        when(appointmentRepository.existsById(appointment1.getId())).thenReturn(true);
        when(appointmentRepository.save(appointment1)).thenReturn(appointment1);
        assertThat(appointmentService.update(appointment1)).isEqualTo(appointment1);
    }

    @Test
    @DisplayName("Test find appointments by user id")
    void testFindAppointmentsByUserId() {
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment1);
        appointments.add(appointment2);
        when(appointmentRepository.findByUserId(appointment1.getUserId())).thenReturn(appointments);
        assertThat(appointmentService.findByUserId(appointment1.getUserId())).isEqualTo(appointments);
    }

    @Test
    @DisplayName("Test find appointments by pet id")
    void testFindAppointmentsByPetId() {
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment1);
        appointments.add(appointment2);
        when(appointmentRepository.findByPetId(appointment1.getPetId())).thenReturn(appointments);
        assertThat(appointmentService.findByPetId(appointment1.getPetId())).isEqualTo(appointments);
    }

    @Test
    @DisplayName("Test find appointments by vet id")
    void testFindAppointmentsByVetId() {
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment1);
        appointments.add(appointment2);
        when(appointmentRepository.findByVetId(appointment1.getVetId())).thenReturn(appointments);
        assertThat(appointmentService.findByVetId(appointment1.getVetId())).isEqualTo(appointments);
    }

    @Test
    @DisplayName("Test find appointments by date")
    void testFindAppointmentsByDate() {
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment1);
        appointments.add(appointment2);
        when(appointmentRepository.findByDate(appointment1.getDate())).thenReturn(appointments);
        assertThat(appointmentService.findByDate(appointment1.getDate())).isEqualTo(appointments);
    }

    @Test
    @DisplayName("Test find appointments by status")
    void testFindAppointmentsByStatus() {
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment1);
        appointments.add(appointment2);
        when(appointmentRepository.findByStatus(appointment1.getStatus())).thenReturn(appointments);
        assertThat(appointmentService.findByStatus(appointment1.getStatus())).isEqualTo(appointments);
    }
    
}
