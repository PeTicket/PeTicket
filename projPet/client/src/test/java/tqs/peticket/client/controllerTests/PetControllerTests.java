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
import tqs.peticket.client.controller.PetController;
import tqs.peticket.client.model.Pet;
import tqs.peticket.client.security.jwt.AuthHandler;
import tqs.peticket.client.service.PetService;
import tqs.peticket.client.serviceTests.Auth;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
@ContextConfiguration(classes = {PetController.class, TestSecurityConfig.class, Auth.class})
class PetControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PetService petService;

    @MockBean
    private AuthHandler authHandler;

    private Pet pet1;
    private Pet pet2;
    private UUID userId;

    @BeforeEach
    public void setUp() throws Exception {
        userId = UUID.randomUUID();
        pet1 = new Pet(userId, "Bella", "Dog", "Labrador", "Black", "2");
        pet2 = new Pet(userId, "Luna", "Cat", "Siamese", "White", "3");
    }

    @Test
    void whenGetAllPets_thenReturnPets() throws Exception {
        List<Pet> allPets = Arrays.asList(pet1, pet2);

        when(petService.getAllPets()).thenReturn(allPets);

        mvc.perform(get("/api/client/pet/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Bella")))
                .andExpect(jsonPath("$[1].name", is("Luna")));

        verify(petService, times(1)).getAllPets();
    }

    @Test
    void whenGetAllPets_thenReturnNoContent() throws Exception {
        when(petService.getAllPets()).thenReturn(List.of());

        mvc.perform(get("/api/client/pet/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(petService, times(1)).getAllPets();
    }

    @Test
    void whenGetPetsByUserId_thenReturnPets() throws Exception {
        List<Pet> userPets = Arrays.asList(pet1, pet2);

        when(authHandler.getUserId()).thenReturn(userId);
        when(petService.findByUserId(userId)).thenReturn(userPets);

        mvc.perform(get("/api/client/pet/by-user-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Bella")))
                .andExpect(jsonPath("$[1].name", is("Luna")));

        verify(petService, times(1)).findByUserId(userId);
    }

    @Test
    void whenGetPetsByUserId_thenReturnNoContent() throws Exception {
        when(authHandler.getUserId()).thenReturn(userId);
        when(petService.findByUserId(userId)).thenReturn(List.of());

        mvc.perform(get("/api/client/pet/by-user-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(petService, times(1)).findByUserId(userId);
    }

    @Test
    void whenAddPet_thenReturnCreated() throws Exception {
        when(authHandler.getUserId()).thenReturn(userId);

        mvc.perform(post("/api/client/pet/add")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(pet1)))
                .andExpect(status().isCreated());

        verify(petService, times(1)).save(any(Pet.class));
    }

    @Test
    void whenAddPet_thenReturnBadRequest() throws Exception {
        mvc.perform(post("/api/client/pet/add")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(null)))
                .andExpect(status().isBadRequest());

        verify(petService, never()).save(any(Pet.class));
    }

    @Test
    void whenUpdatePet_thenReturnUpdatedPet() throws Exception {
        UUID petId = UUID.randomUUID();
        pet1.setId(petId);

        when(authHandler.getUserId()).thenReturn(userId);
        when(petService.update(any(Pet.class))).thenReturn(pet1);

        mvc.perform(put("/api/client/pet/update/" + petId)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(pet1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Bella")));

        verify(petService, times(1)).update(any(Pet.class));
    }

    @Test
    void whenUpdatePet_thenReturnBadRequest() throws Exception {
        mvc.perform(put("/api/client/pet/update/" + UUID.randomUUID())
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(null)))
                .andExpect(status().isBadRequest());

        verify(petService, never()).update(any(Pet.class));
    }

    @Test
    void whenUpdatePet_thenReturnUnauthorized() throws Exception {
        UUID petId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        pet1.setId(petId);
        pet1.setUserId(otherUserId);

        when(authHandler.getUserId()).thenReturn(userId);

        mvc.perform(put("/api/client/pet/update/" + petId)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(pet1)))
                .andExpect(status().isUnauthorized());

        verify(petService, never()).update(any(Pet.class));
    }

    @Test
    void whenDeletePet_thenReturnOk() throws Exception {
        UUID petId = UUID.randomUUID();
        pet1.setId(petId);

        when(authHandler.getUserId()).thenReturn(userId);
        when(petService.findById(petId)).thenReturn(pet1);

        mvc.perform(delete("/api/client/pet/delete/" + petId)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(petService, times(1)).deleteById(petId);
    }

    @Test
    void whenDeletePet_thenReturnNotFound() throws Exception {
        UUID petId = UUID.randomUUID();

        when(authHandler.getUserId()).thenReturn(userId);
        when(petService.findById(petId)).thenReturn(null);

        mvc.perform(delete("/api/client/pet/delete/" + petId)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(petService, never()).deleteById(petId);
    }

    @Test
    void whenDeletePet_thenReturnUnauthorized() throws Exception {
        UUID petId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        pet1.setId(petId);
        pet1.setUserId(otherUserId);

        when(authHandler.getUserId()).thenReturn(userId);
        when(petService.findById(petId)).thenReturn(pet1);

        mvc.perform(delete("/api/client/pet/delete/" + petId)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(petService, never()).deleteById(petId);
    }

    @Test
    void whenGetPetsByName_thenReturnPets() throws Exception {
        List<Pet> petsByName = List.of(pet1, pet2);

        when(authHandler.getUserId()).thenReturn(userId);
        when(petService.findByName("Bella")).thenReturn(petsByName);

        mvc.perform(get("/api/client/pet/by-name/name?name=Bella"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name", is("Bella")));

        verify(petService, times(1)).findByName("Bella");
    }

    @Test
    void whenGetPetsByName_thenReturnNoContent() throws Exception {
        when(authHandler.getUserId()).thenReturn(userId);
        when(petService.findByName("Bella")).thenReturn(List.of());

        mvc.perform(get("/api/client/pet/by-name/name?name=Bella"))
                .andExpect(status().isNoContent());

        verify(petService, times(1)).findByName("Bella");
    }

    @Test
    void whenGetPetsByName_thenReturnNotFound() throws Exception {
        UUID otherUserId = UUID.randomUUID();
        Pet pet = new Pet(otherUserId, "Bella", "Dog", "Labrador", "Brown", "3");

        when(authHandler.getUserId()).thenReturn(userId);
        when(petService.findByName("Bella")).thenReturn(List.of(pet));

        mvc.perform(get("/api/client/pet/by-name/name?name=Bella"))
                .andExpect(status().isNotFound());

        verify(petService, times(1)).findByName("Bella");
    }

    @Test
    void whenGetPetById_thenReturnPet() throws Exception {
        UUID petId = UUID.randomUUID();
        pet1.setId(petId);

        when(authHandler.getUserId()).thenReturn(userId);
        when(petService.findById(petId)).thenReturn(pet1);

        mvc.perform(get("/api/client/pet/by-id/id?id=" + petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Bella")));

        verify(petService, times(1)).findById(petId);
    }

    @Test
    void whenGetPetById_thenReturnNotFound() throws Exception {
        UUID petId = UUID.randomUUID();

        when(authHandler.getUserId()).thenReturn(userId);
        when(petService.findById(petId)).thenReturn(null);

        mvc.perform(get("/api/client/pet/by-id/id?id=" + petId))
                .andExpect(status().isNotFound());

        verify(petService, times(1)).findById(petId);
    }
}