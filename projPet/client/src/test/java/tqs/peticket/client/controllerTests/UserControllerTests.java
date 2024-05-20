package tqs.peticket.client.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import tqs.peticket.client.controller.UserController;
import tqs.peticket.client.service.UserService;
import tqs.peticket.client.serviceTests.Auth;
import tqs.peticket.client.model.User;
import tqs.peticket.client.security.jwt.AuthHandler;


@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {UserController.class, TestSecurityConfig.class, Auth.class})
class UserControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;
    
    @MockBean
    private AuthHandler authHandler;

    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    void whenPostUser_thenCreateUser( ) throws Exception {
        User pedro = new User("Pedro", "Lopes", "pdl@deti.com", "abcd1234", "Rua Esquerda, 274", "964434567");

        when(service.save(Mockito.any())).thenReturn(pedro);

        mvc.perform(
                post("/api/client/user/save").with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(pedro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Pedro")));

        verify(service, times(1)).save(Mockito.any());

    }

    @Test
    void whenSaveUser_ThenUserSaved() throws Exception {
        User pedro = new User("Pedro", "Lopes", "pdl@deti.com", "abcd1234", "Rua Esquerda, 274", "964434567");

        when(authHandler.getUserId()).thenReturn(UUID.randomUUID());

        when(service.save(any())).thenReturn(null);

        mvc.perform(post("/api/client/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(pedro)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenDeleteUser_ThenUserNotInDatabase() throws Exception {
        User pedro = new User("Pedro", "Lopes", "pdl@deti.com", "abcd1234", "Rua Esquerda, 274", "964434567");
        when(service.save(Mockito.any())).thenReturn(pedro);

        mvc.perform(post("/api/client/user/save")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(pedro)));
                
        mvc.perform(delete("/api/client/user/delete"))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(Mockito.any());
        verify(service, never()).findById(Mockito.any());
    }

    @Test
    void whenGetAllUsers_thenReturnUserList() throws Exception {
        User pedro = new User("Pedro", "Lopes", "pdl@deti.com", "abcd1234", "Rua Esquerda, 274", "964434567");
        User maria = new User("Maria", "Santos", "maria@deti.com", "abcd1234", "Rua Direita, 123", "964434568");
        List<User> allUsers = Arrays.asList(pedro, maria);

        when(service.getAllUsers()).thenReturn(allUsers);

        mvc.perform(get("/api/client/user/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("Pedro")))
                .andExpect(jsonPath("$[1].firstName", is("Maria")));

        verify(service, times(1)).getAllUsers();
    }

    @Test
    void whenGetAllUsers_thenReturnNoContent() throws Exception {
        when(service.getAllUsers()).thenReturn(Collections.emptyList());

        mvc.perform(get("/api/client/user/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service, times(1)).getAllUsers();
    }

    @Test
    void whenGetUserByEmail_thenReturnUser() throws Exception {
        User pedro = mock(User.class);
        UUID userId = UUID.randomUUID();

        when(authHandler.getUserId()).thenReturn(userId);
        when(service.findByEmail("pdl@deti.com")).thenReturn(pedro);
        when(pedro.getId()).thenReturn(userId);

        mvc.perform(get("/api/client/user/by-email/pdl@deti.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(pedro.getFirstName())));

        verify(service, times(1)).findByEmail("pdl@deti.com");
    }

    @Test
    void whenGetUserByEmail_thenReturnNoContent() throws Exception {
        when(service.findByEmail("pdl@deti.com")).thenReturn(null);

        mvc.perform(get("/api/client/user/by-email/pdl@deti.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service, times(1)).findByEmail("pdl@deti.com");
    }

    @Test
    void whenGetUserByEmail_thenReturnUnauthorized() throws Exception {
        User pedro = mock(User.class);
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        when(authHandler.getUserId()).thenReturn(userId);
        when(service.findByEmail("pdl@deti.com")).thenReturn(pedro);
        when(pedro.getId()).thenReturn(otherUserId);

        mvc.perform(get("/api/client/user/by-email/pdl@deti.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(service, times(1)).findByEmail("pdl@deti.com");
    }

    @Test
    void whenGetUserById_thenReturnUser() throws Exception {
        User pedro = new User("Pedro", "Lopes", "pdl@deti.com", "abcd1234", "Rua Esquerda, 274", "964434567");
        UUID userId = UUID.randomUUID();

        when(authHandler.getUserId()).thenReturn(userId);
        when(service.findById(userId)).thenReturn(pedro);

        mvc.perform(get("/api/client/user/by-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Pedro")));

        verify(service, times(1)).findById(userId);
    }

    @Test
    void whenGetUserById_thenReturnNoContent() throws Exception {
        UUID userId = UUID.randomUUID();

        when(authHandler.getUserId()).thenReturn(userId);
        when(service.findById(userId)).thenReturn(null);

        mvc.perform(get("/api/client/user/by-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service, times(1)).findById(userId);
    }
}