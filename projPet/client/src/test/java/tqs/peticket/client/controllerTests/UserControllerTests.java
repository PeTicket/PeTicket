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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import tqs.peticket.client.controller.UserController;
import tqs.peticket.client.service.UserService;
import tqs.peticket.client.serviceTests.Auth;
import tqs.peticket.client.model.User;


@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {UserController.class, TestSecurityConfig.class, Auth.class})
class UserControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;

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
    
}