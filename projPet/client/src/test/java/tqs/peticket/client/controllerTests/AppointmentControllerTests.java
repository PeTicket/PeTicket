package tqs.peticket.client.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
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

    @Test
    void whenGetAppointmentsByPetId_thenReturnAppointments() throws Exception {
        when(authHandler.getUserId()).thenReturn(userId1);
        when(aptmService.existsByPetId(petId1)).thenReturn(true);
        when(aptmService.findByPetId(petId1)).thenReturn(Collections.singletonList(aptm1));

        mvc.perform(get("/api/client/appointment/by-pet-id/petId?petId=" + petId1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date", is("30/05/2024")));

        verify(aptmService, times(1)).findByPetId(petId1);
    }

    @Test
    void whenGetAppointmentsByPetId_thenReturnNotFound() throws Exception {
        when(authHandler.getUserId()).thenReturn(userId1);
        when(aptmService.existsByPetId(petId1)).thenReturn(false);

        mvc.perform(get("/api/client/appointment/by-pet-id/petId?petId=" + petId1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(aptmService, times(1)).existsByPetId(petId1);
    }

    @Test
    void whenGetAppointmentsByPetId_thenReturnNoContent() throws Exception {
        when(authHandler.getUserId()).thenReturn(userId1);
        when(aptmService.existsByPetId(petId1)).thenReturn(true);
        when(aptmService.findByPetId(petId1)).thenReturn(Collections.emptyList());

        mvc.perform(get("/api/client/appointment/by-pet-id/petId?petId=" + petId1)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

        verify(aptmService, times(1)).existsByPetId(petId1);
    }

    @Test
    void whenGetAppointmentsByPetId_thenReturnUnauthorized() throws Exception {
        when(authHandler.getUserId()).thenReturn(userId1);
        when(aptmService.existsByPetId(petId1)).thenReturn(true);
        when(aptmService.findByPetId(petId1)).thenReturn(Collections.singletonList(aptm2));

        mvc.perform(get("/api/client/appointment/by-pet-id/petId?petId=" + petId1)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());

        verify(aptmService, times(1)).existsByPetId(petId1);
    }

    @Test
    void whenGetAppointmentById_thenReturnAppointment() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(authHandler.getUserId()).thenReturn(userId1);
        when(aptmService.findById(randomId)).thenReturn(aptm1);

        mvc.perform(get("/api/client/appointment/by-id/id?id=" + randomId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date", is("30/05/2024")));

        verify(aptmService, times(1)).findById(randomId);
    }

    @Test
    void whenGetAppointmentById_thenReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(authHandler.getUserId()).thenReturn(userId1);
        when(aptmService.findById(randomId)).thenReturn(null);

        mvc.perform(get("/api/client/appointment/by-id/id?id=" + randomId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(aptmService, times(1)).findById(randomId);
    }

    @Test
    void whenGetAppointmentById_thenReturnUnauthorized() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(authHandler.getUserId()).thenReturn(userId1);
        when(aptmService.findById(randomId)).thenReturn(aptm2);

        mvc.perform(get("/api/client/appointment/by-id/id?id=" + randomId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(aptmService, times(1)).findById(randomId);
    }

    @Test
    void whenAddAppointment_thenReturnCreated() throws Exception {
        when(authHandler.getUserId()).thenReturn(userId1);
        when(petService.existsById(petId1)).thenReturn(true);
        when(qrCodeService.generateCompressedQRCodeImage(any(UUID.class), any(UUID.class), any(UUID.class)))
                .thenReturn(new byte[0]);

        mvc.perform(post("/api/client/appointment/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"" + userId1 + "\", \"petId\":\"" + petId1 + "\", \"date\":\"30/05/2024\", \"time\":\"17:00\", \"occurence\":\"Infected Eye\", \"status\":\"scheduled\"}"))
                .andExpect(status().isCreated());

        verify(aptmService, times(1)).save(any(Appointment.class));
    }

    @Test
    void whenAddAppointment_thenReturnNotFound() throws Exception {
        when(authHandler.getUserId()).thenReturn(userId1);
        when(petService.existsById(petId1)).thenReturn(false);

        mvc.perform(post("/api/client/appointment/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"" + userId1 + "\", \"petId\":\"" + petId1 + "\", \"date\":\"30/05/2024\", \"time\":\"17:00\", \"occurence\":\"Infected Eye\", \"status\":\"scheduled\"}"))
                .andExpect(status().isNotFound());

        verify(aptmService, times(0)).save(any(Appointment.class));
    }

    @Test
    void whenUpdateAppointment_givenNullAppointment_thenReturnBadRequest() throws Exception {
        when(authHandler.getUserId()).thenReturn(UUID.randomUUID());

        mvc.perform(put("/api/client/appointment/update")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenUpdateAppointment_thenReturnUnauthorized() throws Exception {
        when(authHandler.getUserId()).thenReturn(userId2);
        when(petService.existsById(aptm1.getPetId())).thenReturn(true);
        when(aptmService.findById(aptm1.getId())).thenReturn(aptm1);

        mvc.perform(put("/api/client/appointment/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(aptm1)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenUpdateAppointment_thenReturnOk() throws Exception {
        aptm1.setUserId(authHandler.getUserId());
        when(authHandler.getUserId()).thenReturn(aptm1.getUserId());
        when(petService.existsById(aptm1.getPetId())).thenReturn(true);
        when(aptmService.findById(aptm1.getId())).thenReturn(aptm1);

        mvc.perform(put("/api/client/appointment/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(aptm1)))
                .andExpect(status().isOk());
    }
    

    @Test
    void whenUpdateAppointment_givenNonExistingPet_thenReturnNotFound() throws Exception {
        UUID petId = UUID.randomUUID();

        when(authHandler.getUserId()).thenReturn(userId1);
        when(petService.existsById(petId)).thenReturn(false);

        mvc.perform(put("/api/client/appointment/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(aptm1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenUpdateAppointment_givenNonExistingAppointment_thenReturnNotFound() throws Exception {
        UUID petId = UUID.randomUUID();

        when(authHandler.getUserId()).thenReturn(userId1);
        when(petService.existsById(petId)).thenReturn(true);
        when(aptmService.findById(aptm1.getId())).thenReturn(null);

        mvc.perform(put("/api/client/appointment/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(aptm1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeleteAppointment_thenReturnOk() throws Exception {
        UUID newId = UUID.randomUUID();
        aptm1.setId(newId);

        when(authHandler.getUserId()).thenReturn(aptm1.getUserId());
        when(aptmService.findById(newId)).thenReturn(aptm1);

        mvc.perform(delete("/api/client/appointment/delete/" + newId)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(aptmService, times(1)).delete(aptm1);
    }

    @Test
    void whenDeleteAppointment_thenReturnNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(authHandler.getUserId()).thenReturn(userId1);
        when(aptmService.findById(randomId)).thenReturn(null);

        mvc.perform(delete("/api/client/appointment/delete/{id}", randomId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(aptmService, times(0)).delete(any(Appointment.class));
    }

    @Test
    void whenDeleteAppointment_thenReturnUnauthorized() throws Exception {
        UUID newId = UUID.randomUUID();
        aptm1.setId(newId);

        when(authHandler.getUserId()).thenReturn(aptm2.getUserId());
        when(aptmService.findById(newId)).thenReturn(aptm1);

        mvc.perform(delete("/api/client/appointment/delete/" + newId)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(aptmService, times(0)).delete(any(Appointment.class));
    }
    
}
