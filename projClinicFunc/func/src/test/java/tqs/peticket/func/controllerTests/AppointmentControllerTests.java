package tqs.peticket.func.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import tqs.peticket.func.controller.AppointmentController;
import tqs.peticket.func.model.Appointment;
import tqs.peticket.func.model.Pet;
import tqs.peticket.func.model.User;
import tqs.peticket.func.repository.FuncRepository;
import tqs.peticket.func.repository.UserRepository;
import tqs.peticket.func.service.AppointmentService;
import tqs.peticket.func.service.PetService;
import tqs.peticket.func.service.UserService;
import tqs.peticket.func.serviceTests.Auth;

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
@ContextConfiguration(classes = {AppointmentController.class, TestSecurityConfig.class, Auth.class})
class AppointmentControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private UserService userService;

    @MockBean
    private PetService petService;

    @MockBean
    private FuncRepository funcRepository;

    @MockBean
    private UserRepository userrep;

    @MockBean
    private PasswordEncoder pwdncd;

    @InjectMocks
    private AppointmentController appointmentController;

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


    @Test
    void whenCreateAppointment_thenReturnCreated() throws Exception {
        UUID userid = UUID.randomUUID();
        UUID petid = UUID.randomUUID();
        
        when(petService.findById(petid)).thenReturn(pet1);
        when(userService.findById(userid)).thenReturn(user1);
        
        Appointment aptm = new Appointment(userid, petid, "31/05/2024", "19:00", "Infected Toe", "scheduled");
        when(appointmentService.save(any(Appointment.class))).thenReturn(aptm);

        mvc.perform(post("/api/func/appointment/appointment/{petId}/{userId}", petid, userid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(aptm)))
                .andExpect(status().isCreated());

        verify(appointmentService, times(1)).save(any(Appointment.class));
    }
    
    @Test
    void whenCreateAppointment_thenReturnNotFound() throws Exception {
        UUID userid = UUID.randomUUID();
        UUID petid = UUID.randomUUID();
        
        when(petService.findById(petid)).thenReturn(pet1);
        when(userService.findById(userid)).thenReturn(user1);
        
        Appointment aptm = new Appointment(userid, petid, "31/05/2024", "19:00", "Infected Toe", "scheduled");
        when(appointmentService.save(any(Appointment.class))).thenReturn(null);

        mvc.perform(post("/api/func/appointment/appointment/{petId}/{userId}", petid, userid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(aptm)))
                .andExpect(status().isBadRequest());

        verify(petService, times(1)).findById(petid);
        verify(userService, times(1)).findById(userid);
        verify(appointmentService, times(1)).save(any(Appointment.class));
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

    @Test
    void whenDeleteAppointment_thenReturnNoContent() throws Exception {
        UUID uuid = UUID.randomUUID();
        Appointment appt = new Appointment();
        appt.setId(uuid);
        when(appointmentService.findById(uuid)).thenReturn(appt);

        mvc.perform(delete("/api/func/appointment/appointment/{appointmentId}", uuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(appointmentService, times(1)).findById(uuid);
        verify(appointmentService, times(1)).delete(any(Appointment.class));
    }

    @Test
    void whenDeleteAppointment_thenReturnNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();
        Appointment appt = new Appointment();
        appt.setId(uuid);
        when(appointmentService.findById(uuid)).thenReturn(null);

        mvc.perform(delete("/api/func/appointment/appointment/{appointmentId}", uuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(appointmentService, times(1)).findById(uuid);
        verify(appointmentService, times(0)).delete(any(Appointment.class));
    }

    @Test
    void testUpdateQrCode_AppointmentExists() throws Exception {
        UUID my_uuid = UUID.randomUUID();
        Appointment appoint = new Appointment();
        appoint.setId(my_uuid);
        appoint.setStatus("scheduled");

        when(appointmentService.findById(my_uuid)).thenReturn(appoint);
        when(appointmentService.save(any(Appointment.class))).thenReturn(appoint);

        mvc.perform(put("/api/func/appointment/appointmentQrCode/" + my_uuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("on_hold"));
    }

    @Test
    void testUpdateQrCode_AppointmentNotFound() throws Exception {
        UUID my_uuid = UUID.randomUUID();
        Appointment appoint = new Appointment();
        appoint.setId(my_uuid);
        appoint.setStatus("scheduled");

        when(appointmentService.findById(my_uuid)).thenReturn(null);

        mvc.perform(put("/api/func/appointment/appointmentQrCode/" + my_uuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}

