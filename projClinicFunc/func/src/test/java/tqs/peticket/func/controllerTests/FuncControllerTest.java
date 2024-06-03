// package tqs.peticket.func.controllerTests;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.ContextConfiguration;
// import org.springframework.test.web.servlet.MockMvc;
// import tqs.peticket.func.controller.FuncController;
// import tqs.peticket.func.model.Pet;
// import tqs.peticket.func.model.User;
// import tqs.peticket.func.service.AppointmentService;
// import tqs.peticket.func.service.FuncService;
// import tqs.peticket.func.service.PetService;
// import tqs.peticket.func.service.UserService;

// import java.util.Collections;
// import java.util.List;
// import java.util.UUID;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest(FuncController.class)
// @ContextConfiguration(classes = {FuncController.class, TestSecurityConfig.class})
// class FuncControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private FuncService funcService;

//     @MockBean
//     private UserService userService;

//     @MockBean
//     private PetService petService;

//     @MockBean
//     private AppointmentService appointmentService;

//     @InjectMocks
//     private FuncController funcController;

//     private User user;
//     private Pet pet;

//     @BeforeEach
//     public void setUp() {
//         user = new User();
//         user.setId(UUID.randomUUID());
//         user.setEmail("test@example.com");
//         user.setPassword("password");

//         pet = new Pet();
//         pet.setId(UUID.randomUUID());
//         pet.setUserId(user.getId());
//     }


//     @Test
//     public void testCreateClient_Success() throws Exception {
//         when(userService.save(any(User.class))).thenReturn(user);

//         mockMvc.perform(post("/api/func/user/create")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
//                 .andExpect(status().isCreated());

//         verify(userService, times(1)).save(any(User.class));
//     }

//     @Test
//     public void testCreateClient_Failure() throws Exception {
//         when(userService.save(any(User.class))).thenReturn(null);

//         mockMvc.perform(post("/api/func/user/create")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
//                 .andExpect(status().isBadRequest());

//         verify(userService, times(1)).save(any(User.class));
//     }

//     @Test
//     public void testDeleteClient_Success() throws Exception {
//         when(userService.findById(any(UUID.class))).thenReturn(user);

//         mockMvc.perform(delete("/api/func/user/" + user.getId()))
//                 .andExpect(status().isNoContent());

//         verify(userService, times(1)).findById(user.getId());
//         verify(userService, times(1)).delete(user);
//     }

//     @Test
//     public void testDeleteClient_NotFound() throws Exception {
//         when(userService.findById(any(UUID.class))).thenReturn(null);

//         mockMvc.perform(delete("/api/func/user/" + user.getId()))
//                 .andExpect(status().isNotFound());

//         verify(userService, times(1)).findById(user.getId());
//     }

//     @Test
//     public void testUpdateClient_Success() throws Exception {
//         when(userService.findById(any(UUID.class))).thenReturn(user);
//         when(userService.update(any(UUID.class), any(User.class))).thenReturn(user);

//         mockMvc.perform(put("/api/func/user/" + user.getId())
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"email\":\"updated@example.com\",\"password\":\"updatedpassword\"}"))
//                 .andExpect(status().isOk());

//         verify(userService, times(1)).findById(user.getId());
//         verify(userService, times(1)).update(eq(user.getId()), any(User.class));
//     }

//     @Test
//     public void testUpdateClient_NotFound() throws Exception {
//         when(userService.findById(any(UUID.class))).thenReturn(null);

//         mockMvc.perform(put("/api/func/user/" + user.getId())
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"email\":\"updated@example.com\",\"password\":\"updatedpassword\"}"))
//                 .andExpect(status().isNotFound());

//         verify(userService, times(1)).findById(user.getId());
//     }

//     @Test
//     public void testGetAllClients_NonEmptyList() throws Exception {
//         when(userService.getAllUsers()).thenReturn(List.of(user));

//         mockMvc.perform(get("/api/func/users"))
//                 .andExpect(status().isOk());

//         verify(userService, times(1)).getAllUsers();
//     }

//     @Test
//     public void testGetAllClients_EmptyList() throws Exception {
//         when(userService.getAllUsers()).thenReturn(Collections.emptyList());

//         mockMvc.perform(get("/api/func/users"))
//                 .andExpect(status().isNoContent());

//         verify(userService, times(1)).getAllUsers();
//     }

//     @Test
//     public void testGetClientById_Found() throws Exception {
//         when(userService.findById(any(UUID.class))).thenReturn(user);

//         mockMvc.perform(get("/api/func/users/" + user.getId()))
//                 .andExpect(status().isOk());

//         verify(userService, times(1)).findById(user.getId());
//     }

//     @Test
//     public void testGetClientById_NotFound() throws Exception {
//         when(userService.findById(any(UUID.class))).thenReturn(null);

//         mockMvc.perform(get("/api/func/users/" + user.getId()))
//                 .andExpect(status().isNotFound());

//         verify(userService, times(1)).findById(user.getId());
//     }

//     @Test
//     public void testGetClientByEmail_Found() throws Exception {
//         when(userService.findByEmail(anyString())).thenReturn(user);

//         mockMvc.perform(get("/api/func/user/" + user.getEmail()))
//                 .andExpect(status().isOk());

//         verify(userService, times(1)).findByEmail(user.getEmail());
//     }

//     @Test
//     public void testGetClientByEmail_NotFound() throws Exception {
//         when(userService.findByEmail(anyString())).thenReturn(null);

//         mockMvc.perform(get("/api/func/user/" + user.getEmail()))
//                 .andExpect(status().isNotFound());

//         verify(userService, times(1)).findByEmail(user.getEmail());
//     }

//     @Test
//     public void testCreatePet_Success() throws Exception {
//         when(userService.findById(any(UUID.class))).thenReturn(user);
//         when(petService.save(any(Pet.class))).thenReturn(pet);

//         mockMvc.perform(post("/api/func/pet/user/" + user.getId())
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"name\":\"Buddy\"}"))
//                 .andExpect(status().isCreated());

//         verify(userService, times(1)).findById(user.getId());
//         verify(petService, times(1)).save(any(Pet.class));
//     }

//     @Test
//     public void testCreatePet_BadRequest() throws Exception {
//         when(userService.findById(any(UUID.class))).thenReturn(user);
//         when(petService.save(any(Pet.class))).thenReturn(null);

//         mockMvc.perform(post("/api/func/pet/user/" + user.getId())
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"name\":\"Buddy\"}"))
//                 .andExpect(status().isBadRequest());

//         verify(userService, times(1)).findById(user.getId());
//         verify(petService, times(1)).save(any(Pet.class));
//     }

//     @Test
//     public void testCreatePet_UserNotFound() throws Exception {
//         when(userService.findById(any(UUID.class))).thenReturn(null);

//         mockMvc.perform(post("/api/func/pet/user/" + user.getId())
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"name\":\"Buddy\"}"))
//                 .andExpect(status().isNotFound());

//         verify(userService, times(1)).findById(user.getId());
//     }

//     @Test
//     public void testGetPetsByUser_NonEmptyList() throws Exception {
//         when(petService.findByUserId(any(UUID.class))).thenReturn(List.of(pet));

//         mockMvc.perform(get("/api/func/pets/users/" + user.getId()))
//                 .andExpect(status().isOk());

//         verify(petService, times(1)).findByUserId(user.getId());
//     }

//     @Test
//     public void testGetPetsByUser_EmptyList() throws Exception {
//         when(petService.findByUserId(any(UUID.class))).thenReturn(Collections.emptyList());

//         mockMvc.perform(get("/api/func/pets/users/" + user.getId()))
//                 .andExpect(status().isNoContent());
//             verify(petService, times(1)).findByUserId(user.getId());

//     }

//     @Test
//     public void testGetPetById_Found() throws Exception {
//         when(petService.findById(any(UUID.class))).thenReturn(pet);

//         mockMvc.perform(get("/api/func/pet/" + pet.getId()))
//                 .andExpect(status().isOk());

//         verify(petService, times(1)).findById(pet.getId());
//     }

//     @Test
//     public void testGetPetById_NotFound() throws Exception {
//         when(petService.findById(any(UUID.class))).thenReturn(null);

//         mockMvc.perform(get("/api/func/pet/" + pet.getId()))
//                 .andExpect(status().isNotFound());

//         verify(petService, times(1)).findById(pet.getId());
//     }

//     @Test
//     public void testGetAllPets_NonEmptyList() throws Exception {
//         when(petService.getAllPets()).thenReturn(List.of(pet));

//         mockMvc.perform(get("/api/func/pets"))
//                 .andExpect(status().isOk());

//         verify(petService, times(1)).getAllPets();
//     }

//     @Test
//     public void testGetAllPets_EmptyList() throws Exception {
//         when(petService.getAllPets()).thenReturn(Collections.emptyList());

//         mockMvc.perform(get("/api/func/pets"))
//                 .andExpect(status().isNoContent());

//         verify(petService, times(1)).getAllPets();
//     }

//     @Test
//     public void testUpdatePet_Success() throws Exception {
//         when(petService.findById(any(UUID.class))).thenReturn(pet);
//         when(petService.update(any(UUID.class), any(Pet.class))).thenReturn(pet);

//         mockMvc.perform(put("/api/func/pet/" + pet.getId())
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"name\":\"Updated Buddy\"}"))
//                 .andExpect(status().isOk());

//         verify(petService, times(1)).findById(pet.getId());
//         verify(petService, times(1)).update(eq(pet.getId()), any(Pet.class));
//     }

//     @Test
//     public void testUpdatePet_NotFound() throws Exception {
//         when(petService.findById(any(UUID.class))).thenReturn(null);

//         mockMvc.perform(put("/api/func/pet/" + pet.getId())
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"name\":\"Updated Buddy\"}"))
//                 .andExpect(status().isNotFound());

//         verify(petService, times(1)).findById(pet.getId());
//     }

//     @Test
//     public void testDeletePet_Success() throws Exception {
//         when(petService.findById(any(UUID.class))).thenReturn(pet);

//         mockMvc.perform(delete("/api/func/delete-pet/" + pet.getId()))
//                 .andExpect(status().isNoContent());

//         verify(petService, times(1)).findById(pet.getId());
//         verify(petService, times(1)).delete(pet);
//     }

//     @Test
//     public void testDeletePet_NotFound() throws Exception {
//         when(petService.findById(any(UUID.class))).thenReturn(null);

//         mockMvc.perform(delete("/api/func/delete-pet/" + pet.getId()))
//                 .andExpect(status().isNotFound());

//         verify(petService, times(1)).findById(pet.getId());
//     }
// }

