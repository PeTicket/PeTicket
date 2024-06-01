package tqs.peticket.vet.serviceTests;

import tqs.peticket.vet.model.Appointment;
import tqs.peticket.vet.model.Pet;
import tqs.peticket.vet.model.User;
import tqs.peticket.vet.model.Vet;
import tqs.peticket.vet.repository.AppointmentsRepository;
import tqs.peticket.vet.repository.PetRepository;
import tqs.peticket.vet.repository.UserRepository;
import tqs.peticket.vet.repository.VetRepository;
import tqs.peticket.vet.service.AppointmentService;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentsRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private VetRepository vetRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Appointment appointment1;
    private Appointment appointment2;
    private User user;
    private Pet pet;
    private Vet vet;

    @BeforeEach
    void setUp() {
        appointment1 = new Appointment();
        appointment1.setId(UUID.randomUUID());
        appointment1.setUserId(UUID.randomUUID());
        appointment1.setPetId(UUID.randomUUID());
        appointment1.setVetId(UUID.randomUUID());
        appointment1.setDate("2024-05-17");
        appointment1.setTime("15:00");
        appointment1.setStatus("on_hold");
        appointment1.setAppointment_number(1);

        appointment2 = new Appointment();
        appointment2.setId(UUID.randomUUID());
        appointment2.setUserId(UUID.randomUUID());
        appointment2.setPetId(UUID.randomUUID());
        appointment2.setVetId(UUID.randomUUID());
        appointment2.setDate("2024-05-05");
        appointment2.setTime("10:00");
        appointment2.setStatus("on_hold");
        appointment1.setAppointment_number(2);


        user = new User();
        user.setId(UUID.randomUUID());

        pet = new Pet();
        pet.setId(UUID.randomUUID());

        vet = new Vet();
        vet.setId(UUID.randomUUID());
    }

    @Test
    @DisplayName("Test change status of an appointment")
    void testChangeStatus() {
        when(appointmentRepository.existsById(appointment1.getId())).thenReturn(true);
        when(appointmentRepository.findById(appointment1.getId())).thenReturn(appointment1);
        appointmentService.changeStatus(appointment1, "In Progress");
        assertThat(appointment1.getStatus()).isEqualTo("In Progress");
    }

    @Test
    @DisplayName("Test add diagnose to an appointment")
    void testAddDiagnose() {
        when(appointmentRepository.existsById(appointment1.getId())).thenReturn(true);
        when(appointmentRepository.findById(appointment1.getId())).thenReturn(appointment1);
        appointmentService.addDiagnose(appointment1, "Fracture");
        assertThat(appointment1.getDiagnosis()).isEqualTo("Fracture");
    }

    @Test
    @DisplayName("Test add prescription to an appointment")
    void testAddPrescription() {
        when(appointmentRepository.existsById(appointment1.getId())).thenReturn(true);
        when(appointmentRepository.findById(appointment1.getId())).thenReturn(appointment1);
        appointmentService.addPrescription(appointment1, "Antibiotics");
        assertThat(appointment1.getPrescription()).isEqualTo("Antibiotics");
    }

    @Test
    @DisplayName("Test add observations to an appointment")
    void testAddObservations() {
        when(appointmentRepository.existsById(appointment1.getId())).thenReturn(true);
        when(appointmentRepository.findById(appointment1.getId())).thenReturn(appointment1);
        appointmentService.addObservations(appointment1, "Patient stable");
        assertThat(appointment1.getObservations()).isEqualTo("Patient stable");
    }

    @Test
    @DisplayName("Test find appointment by id")
    void testFindAppointmentById() {
        when(appointmentRepository.findById(appointment1.getId())).thenReturn(appointment1);
        assertThat(appointmentService.findById(appointment1.getId())).isEqualTo(appointment1);
    }

    @Test
    @DisplayName("Test exists appointment by id")
    void testExistsAppointmentById() {
        when(appointmentRepository.existsById(appointment1.getId())).thenReturn(true);
        assertThat(appointmentService.existsById(appointment1.getId())).isTrue();
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
    @DisplayName("Test find appointments by date and time")
    void testFindAppointmentsByDateAndTime() {
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment1);
        appointments.add(appointment2);
        when(appointmentRepository.findByDateAndTime(appointment1.getDate(), appointment1.getTime())).thenReturn(appointments);
        assertThat(appointmentService.findByDateAndTime(appointment1.getDate(), appointment1.getTime())).isEqualTo(appointments);
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
    @DisplayName("Test find appointments by time")
    void testFindAppointmentsByTime() {
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment1);
        appointments.add(appointment2);
        when(appointmentRepository.findByTime(appointment1.getTime())).thenReturn(appointments);
        assertThat(appointmentService.findByTime(appointment1.getTime())).isEqualTo(appointments);
    }

    @Test
    @DisplayName("Test get all appointments")
    void testGetAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment1);
        appointments.add(appointment2);
        when(appointmentRepository.findAll()).thenReturn(appointments);
        assertThat(appointmentService.getAllAppointments()).isEqualTo(appointments);
    }

    @Test
    @DisplayName("Test update appointment")
    void testUpdateAppointment() {
        when(appointmentRepository.existsById(appointment1.getId())).thenReturn(true);
        when(appointmentRepository.findById(appointment1.getId())).thenReturn(appointment1);
        when(appointmentRepository.save(appointment1)).thenReturn(appointment1);

        appointment1.setObservations("Updated observations");
        assertThat(appointmentService.update(appointment1.getId(), appointment1)).isEqualTo(appointment1);
    }

    @Test
    @DisplayName("Test update non-existing appointment")
    void testUpdateNonExistingAppointment() {
        when(appointmentRepository.existsById(appointment1.getId())).thenReturn(false);
        assertThat(appointmentService.update(appointment1.getId(), appointment1)).isNull();
    }

    @Test
    @DisplayName("Test save appointment")
    void testSaveAppointment() {
        when(appointmentRepository.save(appointment1)).thenReturn(appointment1);
        assertThat(appointmentService.save(appointment1)).isEqualTo(appointment1);
    }

    @Test
    @DisplayName("Test delete appointment by id")
    void testDeleteAppointmentById() {
        when(appointmentRepository.existsById(appointment1.getId())).thenReturn(true);
        when(appointmentRepository.findById(appointment1.getId())).thenReturn(appointment1);
        doNothing().when(appointmentRepository).delete(appointment1);

        appointmentService.deleteById(appointment1.getId());
        verify(appointmentRepository, times(1)).delete(appointment1);
    }

}
