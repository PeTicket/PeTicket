package tqs.peticket.client.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import tqs.peticket.client.controller.AppointmentController;
import tqs.peticket.client.model.Appointment;
import tqs.peticket.client.security.jwt.AuthHandler;
import tqs.peticket.client.service.AppointmentService;
import tqs.peticket.client.service.PetService;
import tqs.peticket.client.service.QrCodeService;
import tqs.peticket.client.service.UserService;
import tqs.peticket.client.serviceTests.Auth;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
@ContextConfiguration(classes = {AppointmentController.class, TestSecurityConfig.class, Auth.class})
class AppointmentControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AppointmentService aptmService;

    @MockBean
    private AuthHandler authHandler;

    @MockBean
    private UserService userService;

    @MockBean
    private PetService petService;

    @MockBean
    private QrCodeService qrCodeService;

    private Appointment aptm1;
    private Appointment aptm2;
    private UUID userId1, userId2;
    private UUID petId1, petId2;

    @BeforeEach
    public void setUp() throws Exception {
        userId1 = UUID.randomUUID();
        petId1 = UUID.randomUUID();
        userId2 = UUID.randomUUID();
        petId2 = UUID.randomUUID();
        // public Appointment(UUID userId, UUID petId, String date, String time, String occurence, String status) {
        aptm1 = new Appointment(userId1, petId1, "30/05/2024", "17:00", "Infected Eye", "shceduled");
        aptm2 = new Appointment(userId2, petId2, "31/05/2024", "18:00", "Infected Toe", "shceduled");
    }

    @Test
    void whenGetAllAppointments_thenReturnAppoinments() throws Exception {
        List<Appointment> allAptms = Arrays.asList(aptm1, aptm2);
        when(aptmService.getAllAppointments()).thenReturn(allAptms);

        mvc.perform(get("/api/client/appointment/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date", is("30/05/2024")))
                .andExpect(jsonPath("$[1].date", is("31/05/2024")));

        verify(aptmService, times(1)).getAllAppointments();
    }

    @Test
    void whenGetAllAppointments_thenReturnNoContent() throws Exception {
        when(aptmService.getAllAppointments()).thenReturn(Collections.emptyList());

        mvc.perform(get("/api/client/appointment/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(aptmService, times(1)).getAllAppointments();
    }

    @Test
    void whenGetAppointmentByUserId_thenReturnAppointment() throws Exception {
        when(authHandler.getUserId()).thenReturn(userId1);
        when(aptmService.findByUserId(userId1)).thenReturn(Collections.singletonList(aptm1));

        mvc.perform(get("/api/client/appointment/by-user-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date", is("30/05/2024")));

        verify(aptmService, times(1)).findByUserId(userId1);
    }

    @Test
    void whenGetAppointmentByUserId_thenReturnNoContent() throws Exception {
        when(authHandler.getUserId()).thenReturn(userId1);
        when(aptmService.getAllAppointments()).thenReturn(Collections.emptyList());

        mvc.perform(get("/api/client/appointment/by-user-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(aptmService, times(1)).findByUserId(userId1);
    }

    
}
