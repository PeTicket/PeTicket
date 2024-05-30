package tqs.peticket.func.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import tqs.peticket.func.controller.AppointmentController;
import tqs.peticket.func.controller.FuncController;
import tqs.peticket.func.model.Appointment;
import tqs.peticket.func.model.Pet;
import tqs.peticket.func.model.User;
import tqs.peticket.func.service.AppointmentService;
import tqs.peticket.func.service.PetService;
import tqs.peticket.func.service.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
@ContextConfiguration(classes = {AppointmentController.class, TestSecurityConfig.class})
class AppointmentControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private UserService userService;

    @MockBean
    private PetService petService;

    private Appointment aptm1;
    private Appointment aptm2;
    private Pet pet1;
    private Pet pet2;
    private User user1;
    private User user2;
    private UUID userId1;
    private UUID petId1;

    @BeforeEach
    public void setUp() throws Exception {
        userId1 = UUID.randomUUID();
        petId1 = UUID.randomUUID();
        pet1 = new Pet();
        pet2 = new Pet();
        user1 = new User();
        user2 = new User();
        // public Appointment(UUID userId, UUID petId, String date, String time, String occurence, String status) {
 
        aptm1 = new Appointment(user1.getId(), pet1.getId(), "30/05/2024", "17:00", "Infected Eye", "shceduled");
        aptm2 = new Appointment(user2.getId(), pet2.getId(), "31/05/2024", "18:00", "Infected Toe", "shceduled");
    }

    @Test
    void whenGetAllAppointments_thenReturnAppointments() throws Exception {
        List<Appointment> allAppointments = Arrays.asList(aptm1, aptm2);
        when(appointmentService.getAllAppointments()).thenReturn(allAppointments);

        mvc.perform(get("/api/func/appointment/appointments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date", is("30/05/2024")))
                .andExpect(jsonPath("$[1].date", is("31/05/2024")));

        verify(appointmentService, times(1)).getAllAppointments();
    }

    @Test
    void whenGetAllAppointments_thenReturnNoContent() throws Exception {
        when(appointmentService.getAllAppointments()).thenReturn(Collections.emptyList());

        mvc.perform(get("/api/func/appointment/appointments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(appointmentService, times(1)).getAllAppointments();
    }

    @Test
    void whenGetAppointmentById_thenReturnAppointment() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(appointmentService.findById(randomId)).thenReturn(aptm1);

        mvc.perform(get("/api/func/appointment/{id}", randomId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date", is("30/05/2024")));

        verify(appointmentService, times(1)).findById(randomId);
    }

    @Test
    void whenGetAppointmentById_thenReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(appointmentService.findById(randomId)).thenReturn(null);

        mvc.perform(get("/api/func/appointment/{id}", randomId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(appointmentService, times(1)).findById(randomId);
    }

/*
    @Test
    void whenCreateAppointment_thenReturnCreated() throws Exception {
        when(petService.findById(aptm1.getPetId())).thenReturn(pet1);
        when(userService.findById(aptm1.getUserId())).thenReturn(user1);
        UUID userid = UUID.randomUUID();
        UUID petid = UUID.randomUUID();
        Appointment aptm = new Appointment(userid, petid, "31/05/2024", "19:00", "Infected Toe", "shceduled");
        when(appointmentService.save(any(Appointment.class))).thenReturn(aptm);

        mvc.perform(post("/api/func/appointment/{petId}/{userId}", aptm1.getPetId(), aptm1.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(aptm)))
                .andExpect(status().isCreated());

        verify(appointmentService, times(1)).save(any(Appointment.class));
    }

*/
    @Test
    void whenCreateAppointment_thenReturnNotFound() throws Exception {
        when(petService.findById(petId1)).thenReturn(null);
        when(userService.findById(userId1)).thenReturn(null);

        mvc.perform(post("/api/func/appointment/{petId}/{userId}", petId1, userId1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(aptm1)))
                .andExpect(status().isNotFound());

        verify(appointmentService, times(0)).save(any(Appointment.class));
    }

    @Test
    void whenUpdatePrescription_thenReturnUpdated() throws Exception {
        UUID randomId = UUID.randomUUID();
        String newPrescription = "New Prescription";
        when(appointmentService.findById(randomId)).thenReturn(aptm1);
        when(appointmentService.save(any(Appointment.class))).thenReturn(aptm1);

        mvc.perform(put("/api/func/appointment/update-prescription/{id}", randomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newPrescription))
                .andExpect(status().isOk());

        verify(appointmentService, times(1)).findById(randomId);
        verify(appointmentService, times(1)).save(any(Appointment.class));
    }

    @Test
    void whenUpdatePrescription_thenReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        String newPrescription = "New Prescription";
        when(appointmentService.findById(randomId)).thenReturn(null);

        mvc.perform(put("/api/func/appointment/update-prescription/{id}", randomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newPrescription))
                .andExpect(status().isNotFound());

        verify(appointmentService, times(1)).findById(randomId);
        verify(appointmentService, times(0)).save(any(Appointment.class));
    }

    @Test
    void whenGetNextAppointment_thenReturnNextAppointment() throws Exception {
        when(appointmentService.findNextAppointment()).thenReturn(aptm1);

        mvc.perform(post("/api/func/appointment/next")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date", is("30/05/2024")));

        verify(appointmentService, times(1)).findNextAppointment();
        verify(appointmentService, times(1)).save(any(Appointment.class));
    }

    @Test
    void whenGetNextAppointment_thenReturnNoContent() throws Exception {
        when(appointmentService.findNextAppointment()).thenReturn(null);

        mvc.perform(post("/api/func/appointment/next")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(appointmentService, times(1)).findNextAppointment();
        verify(appointmentService, times(0)).save(any(Appointment.class));
    }

/* 
    @Test
    void whenDeleteAppointment_thenReturnNoContent() throws Exception {
        when(appointmentService.findById(aptm1.getId())).thenReturn(aptm1);

        mvc.perform(delete("/api/func/appointment/{appointmentId}", aptm1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(appointmentService, times(1)).findById(aptm1.getId());
        verify(appointmentService, times(1)).delete(any(Appointment.class));
    }

    @Test
    void whenDeleteAppointment_thenReturnNotFound() throws Exception {
        when(appointmentService.findById(aptm1.getId())).thenReturn(null);

        mvc.perform(delete("/api/func/appointment/{appointmentId}", aptm1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(appointmentService, times(1)).findById(aptm1.getId());
        verify(appointmentService, times(0)).delete(any(Appointment.class));
    }

    @Test
    void whenUpdateQrCode_thenReturnUpdated() throws Exception {
        UUID randomId = UUID.randomUUID();
        String newStatusQrCode = "New Status";
        when(appointmentService.findById(randomId)).thenReturn(aptm1);
        when(appointmentService.save(any(Appointment.class))).thenReturn(aptm1);

        mvc.perform(put("/api/func/appointmentQrCode/{appointmentId}", randomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newStatusQrCode))
                .andExpect(status().isOk());

        verify(appointmentService, times(1)).findById(randomId);
        verify(appointmentService, times(1)).save(any(Appointment.class));
    }

    @Test
    void whenUpdateQrCode_thenReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        String newStatusQrCode = "New Status";
        when(appointmentService.findById(randomId)).thenReturn(null);

        mvc.perform(put("/api/func/appointmentQrCode/{appointmentId}", randomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newStatusQrCode))
                .andExpect(status().isNotFound());

        verify(appointmentService, times(1)).findById(randomId);
        verify(appointmentService, times(0)).save(any(Appointment.class));
    }
*/
}

