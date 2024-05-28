package tqs.peticket.vet.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import tqs.peticket.vet.controller.AppointmentController;
import tqs.peticket.vet.model.Appointment;
import tqs.peticket.vet.model.Pet;
import tqs.peticket.vet.security.jwt.AuthHandler;
import tqs.peticket.vet.service.AppointmentService;
import tqs.peticket.vet.service.PetService;
import tqs.peticket.vet.service.VetService;
import tqs.peticket.vet.service.UserService;
import tqs.peticket.vet.serviceTests.Auth;
import tqs.peticket.vet.middleware.SenderConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    private VetService vetService;

    @MockBean
    private SenderConfig senderConfig;

    private Appointment aptm1;
    private Appointment aptm2;
    private UUID userId1, userId2;
    private UUID petId1, petId2;
    private UUID vetId1, vetId2;

    @BeforeEach
    public void setUp() throws Exception {
        userId1 = UUID.randomUUID();
        petId1 = UUID.randomUUID();
        userId2 = UUID.randomUUID();
        petId2 = UUID.randomUUID();
        vetId1 = UUID.randomUUID();
        vetId2 = UUID.randomUUID();
        // public Appointment(UUID userId, UUID petId, String date, String time, String occurence, String status) {
        aptm1 = new Appointment(userId1, petId1, "30/05/2024", "17:00", "Infected Eye", "shceduled");
        aptm2 = new Appointment(userId2, petId2, "31/05/2024", "18:00", "Infected Toe", "shceduled");
        aptm1.setId(UUID.randomUUID());
        aptm2.setId(UUID.randomUUID());
        aptm1.setVetId(vetId1);
        aptm2.setVetId(vetId2);
    }

    @Test
    void whenFindAll_thenReturnAppoinments() throws Exception {
        boolean vet = authHandler.isVet();
        when(authHandler.isVet()).thenReturn(vet);
        List<Appointment> allAptms = Arrays.asList(aptm1, aptm2);
        when(aptmService.findAll()).thenReturn(allAptms);

        mvc.perform(get("/api/vet/appointment/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date", is("30/05/2024")))
                .andExpect(jsonPath("$[1].date", is("31/05/2024")));

        verify(aptmService, times(1)).findAll();
    }

    @Test
    void whenFindAll_thenReturnForbiden() throws Exception {
        boolean vet = authHandler.isVet();
        vet = !vet;
        when(authHandler.isVet()).thenReturn(vet);
        List<Appointment> allAptms = Arrays.asList(aptm1, aptm2);
        when(aptmService.findAll()).thenReturn(allAptms);

        mvc.perform(get("/api/vet/appointment/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(aptmService, times(0)).findAll();
    }

    @Test
    void testGetAppointmentById_Success() throws Exception {
        when(aptmService.findById(aptm1.getId())).thenReturn(aptm1);
        
        mvc.perform(get("/api/vet/appointment/{id}", aptm1.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.date", is(aptm1.getDate())));

        verify(aptmService, times(1)).findById(aptm1.getId());
    }

    @Test
    void testGetAppointmentById_NotFound() throws Exception {
        when(aptmService.findById(Mockito.any())).thenReturn(null);
        
        mvc.perform(get("/api/vet/appointment/{id}", UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(aptmService, times(1)).findById(Mockito.any());
    }

    @Test
    void testUpdateAppointment_Success() throws Exception {
        when(aptmService.findById(aptm1.getId())).thenReturn(aptm1);
        when(authHandler.getUserId()).thenReturn(aptm1.getVetId());
        Appointment updatedAptm = new Appointment(aptm1.getUserId(), aptm1.getPetId(), "31/05/2024", "10:00", "Follow-up", "scheduled");
        when(aptmService.update(eq(aptm1.getId()),any(Appointment.class))).thenReturn(updatedAptm);
        
        mvc.perform(put("/api/vet/appointment/update/{id}", aptm1.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(updatedAptm)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.time", is(updatedAptm.getTime())));

        verify(aptmService, times(1)).findById(aptm1.getId());
    }

    @Test
    void testUpdateAppointment_NotUpdated() throws Exception {
        when(aptmService.findById(aptm1.getId())).thenReturn(aptm1);
        when(authHandler.getUserId()).thenReturn(aptm1.getVetId());
        Appointment updatedAptm = new Appointment(aptm1.getUserId(), aptm1.getPetId(), "31/05/2024", "10:00", "Follow-up", "scheduled");
        when(aptmService.update(eq(aptm1.getId()),any(Appointment.class))).thenReturn(null);
        
        mvc.perform(put("/api/vet/appointment/update/{id}", aptm1.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(updatedAptm)))
            .andExpect(status().isNotFound());

        verify(aptmService, times(1)).findById(aptm1.getId());
    }

    @Test
    void testUpdateAppointment_Unauthorized() throws Exception {
        when(aptmService.findById(aptm1.getId())).thenReturn(aptm2);
        when(authHandler.getUserId()).thenReturn(aptm1.getVetId());
        
        mvc.perform(put("/api/vet/appointment/update/{id}", aptm1.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(aptm1)))
            .andExpect(status().isUnauthorized());

        verify(aptmService, times(1)).findById(aptm1.getId());
        verify(aptmService, times(0)).update(any(), any());
    }

    @Test
    void testUpdateAppointment_NotFound() throws Exception {
        when(authHandler.isVet()).thenReturn(true);
        when(aptmService.findById(Mockito.any())).thenReturn(null);
        
        mvc.perform(put("/api/vet/appointment/update/{id}", UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(aptm1)))
            .andExpect(status().isNotFound());

        verify(aptmService, times(1)).findById(Mockito.any());
        verify(aptmService, times(0)).update(any(), any());
    }

    @Test
    void testDeleteAppointment_Success() throws Exception {
        when(aptmService.findById(aptm1.getId())).thenReturn(aptm1);
        when(authHandler.getUserId()).thenReturn(aptm1.getVetId());
        
        mvc.perform(delete("/api/vet/appointment/{id}", aptm1.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(aptmService, times(1)).findById(aptm1.getId());
        verify(aptmService, times(1)).deleteById(aptm1.getId());
        }

        @Test
        void testDeleteAppointment_Unauthorized() throws Exception {
            when(aptmService.findById(aptm1.getId())).thenReturn(aptm1);
            when(authHandler.getUserId()).thenReturn(aptm2.getVetId());
            
            mvc.perform(delete("/api/vet/appointment/{id}", aptm1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    
            verify(aptmService, times(1)).findById(aptm1.getId());
            verify(aptmService, times(0)).deleteById(aptm1.getId());
            }
    

    @Test
    void testDeleteAppointment_NotFound() throws Exception {
        when(aptmService.findById(aptm1.getId())).thenReturn(null);

        mvc.perform(delete("/api/vet/appointment/{id}", aptm1.getId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

    @Test
    void testUpdatePrescription_Success() throws Exception {
        when(aptmService.findById(aptm1.getId())).thenReturn(aptm1);
        when(authHandler.getUserId()).thenReturn(aptm1.getVetId());
        String prescription = "Prescription details";
        aptm1.setPrescription(prescription);
        when(aptmService.save(any(Appointment.class))).thenReturn(aptm1);
        
        mvc.perform(put("/api/vet/appointment/{id}/{prescription}", aptm1.getId(), prescription))
            .andExpect(status().isOk());

        verify(aptmService, times(1)).findById(aptm1.getId());
        verify(aptmService, times(1)).save(any(Appointment.class));
    }

    @Test
    void testUpdatePrescription_Unauthorized() throws Exception {
        when(aptmService.findById(aptm1.getId())).thenReturn(aptm1);
        when(authHandler.getUserId()).thenReturn(UUID.randomUUID());
        String prescription = "Prescription details";
        
        mvc.perform(put("/api/vet/appointment/{id}/{prescription}", aptm1.getId(), prescription))
            .andExpect(status().isUnauthorized());

        verify(aptmService, times(1)).findById(aptm1.getId());
        verify(aptmService, times(0)).save(any(Appointment.class));
    }

    @Test
    void testUpdatePrescription_BadRequest() throws Exception {
        when(aptmService.findById(aptm1.getId())).thenReturn(aptm1);
        when(authHandler.getUserId()).thenReturn(aptm1.getVetId());
        String prescription = "Prescription details";
        when(aptmService.save(any(Appointment.class))).thenReturn(null);
        
        mvc.perform(put("/api/vet/appointment/{id}/{prescription}", aptm1.getId(), prescription))
            .andExpect(status().isBadRequest());

        verify(aptmService, times(1)).findById(aptm1.getId());
        verify(aptmService, times(1)).save(any(Appointment.class));
    }

    @Test
    void testUpdatePrescription_AppointmentNotFound() throws Exception {
        when(aptmService.findById(aptm1.getId())).thenReturn(null);
        String prescription = "Prescription details";
        
        mvc.perform(put("/api/vet/appointment/{id}/{prescription}", aptm1.getId(), prescription))
            .andExpect(status().isNotFound());

        verify(aptmService, times(1)).findById(aptm1.getId());
        verify(aptmService, times(0)).save(any(Appointment.class));
    }

    @Test
    void testGetNextAppointment_Success() throws Exception {
        when(aptmService.findNextAppointment()).thenReturn(aptm2);
        String clinicNumber = "12345";
        
        mvc.perform(post("/api/vet/appointment/next/{clinicNumber}", clinicNumber)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(aptmService, times(1)).findNextAppointment();
    }

    @Test
    void testGetNextAppointment_NoAppointmentsFound() throws Exception {
        when(aptmService.findNextAppointment()).thenReturn(null);
        String clinicNumber = "12345";
        
        mvc.perform(post("/api/vet/appointment/next/{clinicNumber}", clinicNumber)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(aptmService, times(1)).findNextAppointment();
    }

}