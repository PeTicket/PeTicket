package tqs.peticket.vet.controllerTests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import tqs.peticket.vet.controller.AppointmentController;
import tqs.peticket.vet.controller.VetController;
import tqs.peticket.vet.model.Pet;
import tqs.peticket.vet.model.User;
import tqs.peticket.vet.service.PetService;
import tqs.peticket.vet.service.UserService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VetController.class)
@ContextConfiguration(classes = {VetController.class, TestSecurityConfig.class})
public class VetControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PetService petService;

    private User user;
    private Pet pet;
    private UUID userId;
    private UUID petId;

    @BeforeEach
    void setUp() throws Exception {
        userId = UUID.randomUUID();
        petId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        
        pet = new Pet();
        pet.setId(petId);
        pet.setName("Buddy");
        pet.setUserId(userId);
    }

    @Test
    public void getAllUsersTest() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/api/vet/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userId.toString()));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void getAllUsers_NoUsersFoundTest() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/vet/users"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void getUserByEmailTest() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(user);

        mockMvc.perform(get("/api/vet/users/{email}", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()));

        verify(userService, times(1)).findByEmail(anyString());        
    }

    @Test
    public void getUserByEmailTest_NoUsersFound() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(null);

        mockMvc.perform(get("/api/vet/users/{email}", "best@example.com"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).findByEmail(anyString());        
    }

    @Test
    public void getUserByIdTest() throws Exception {
        when(userService.findById(userId)).thenReturn(user);

        mockMvc.perform(get("/api/vet/users/by-id/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()));

        verify(userService, times(1)).findById(userId);
    }

    @Test
    public void getUserById_NoUserTest() throws Exception {
        when(userService.findById(userId)).thenReturn(null);

        mockMvc.perform(get("/api/vet/users/by-id/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).findById(userId);
    }

    @Test
    public void getAllPetsWhenPetsExist() throws Exception {
        when(petService.getAllPets()).thenReturn(Collections.singletonList(pet));

        mockMvc.perform(get("/api/vet/pets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Buddy"));

        verify(petService, times(1)).getAllPets();
    }

    @Test
    public void getAllPetsWhenNoPetsExist() throws Exception {
        when(petService.getAllPets()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/vet/pets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(petService, times(1)).getAllPets();
    }

    @Test
    public void getPetsByUserIdWhenPetsExist() throws Exception {
        when(petService.findByUserId(userId)).thenReturn(Collections.singletonList(pet));

        mockMvc.perform(get("/api/vet/pets/user/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Buddy"));

        verify(petService, times(1)).findByUserId(userId);
    }

    @Test
    public void getPetsByUserIdWhenNoPetsExist() throws Exception {
        when(petService.findByUserId(userId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/vet/pets/user/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(petService, times(1)).findByUserId(userId);
    }

    @Test
    public void getPetsByNameWhenPetsExist() throws Exception {

        when(petService.findByName(pet.getName())).thenReturn(Collections.singletonList(pet));

        mockMvc.perform(get("/api/vet/pets/name/{name}", pet.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$").isArray()) 
                .andExpect(jsonPath("$[0].name").value("Buddy"));

        verify(petService, times(1)).findByName(pet.getName());
    }

    @Test
    public void getPetsByNameWhenNoPetsExist() throws Exception {
    
        when(petService.findByName(pet.getName())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/vet/pets/name/{name}", pet.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); 

        verify(petService, times(1)).findByName(pet.getName());
    }

    @Test
    public void getPetByIdWhenPetExists() throws Exception {
        when(petService.findById(pet.getId())).thenReturn(pet);

        mockMvc.perform(get("/api/vet/pets/{id}", pet.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.name").value("Buddy"));

        verify(petService, times(1)).findById(pet.getId());
    }

    @Test
    public void getPetByIdWhenPetNotFound() throws Exception {
        when(petService.findById(pet.getId())).thenReturn(null);

        mockMvc.perform(get("/api/vet/pets/{id}", pet.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); 

        verify(petService, times(1)).findById(pet.getId());
    }

    @Test
    public void updatePetWhenPetExists() throws Exception {
        when(petService.update(petId, pet)).thenReturn(pet);

        mockMvc.perform(put("/api/vet/pets/{id}", pet.getId())
                .content(JsonUtils.toJson(pet))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Buddy"));

        verify(petService, times(1)).update(pet.getId(), pet);
    }

    @Test
    public void updatePetWhenPetNotFound() throws Exception {
        when(petService.update(pet.getId(), pet)).thenReturn(null);

        mockMvc.perform(put("/api/vet/pets/{id}", pet.getId())
                .content(JsonUtils.toJson(pet))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(petService, times(1)).update(pet.getId(), pet);
    }
}
