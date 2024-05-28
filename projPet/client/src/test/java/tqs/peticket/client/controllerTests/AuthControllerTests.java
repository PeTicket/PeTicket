package tqs.peticket.client.controllerTests;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.Authentication;

import tqs.peticket.client.controller.AuthController;
import tqs.peticket.client.model.User;
import tqs.peticket.client.repository.UserRepository;
import tqs.peticket.client.security.jwt.JwtUtils;
import tqs.peticket.client.service.UserService;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = {AuthController.class, TestSecurityConfig.class})
class AuthControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserService userService;

    private User existingUser;
    private User newUser;

    @BeforeEach
    public void setUp() {
        existingUser = new User("existing@example.com", "password123", "ua@ua.pt", "rua", "123456789");
        newUser = new User("new@example.com", "newpassword123", "pop@ua.pt", "addr", "987654321");
    }

    @Test
    void testAuthenticateUser_Success() throws Exception {
        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(existingUser);
        when(passwordEncoder.matches(existingUser.getPassword(), existingUser.getPassword())).thenReturn(true);
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("mockedJWT");

        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(existingUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("mockedJWT"));
    }

    @Test
    void testAuthenticateUser_InvalidPassword() throws Exception {
        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(existingUser);
        when(passwordEncoder.matches(existingUser.getPassword(), existingUser.getPassword())).thenReturn(false);

        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(existingUser)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAuthenticateUser_UserNotFound() throws Exception {
        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(null);

        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(existingUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(newUser.getPassword())).thenReturn(newUser.getPassword());

        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(newUser)))
                .andExpect(status().isCreated());
    }

    @Test
    void testRegisterUser_UserAlreadyExists() throws Exception {
        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(existingUser);

        mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJson(existingUser)))
                .andExpect(status().isConflict());
    }
}

