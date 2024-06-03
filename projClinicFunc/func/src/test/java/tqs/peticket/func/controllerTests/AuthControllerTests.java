// package tqs.peticket.func.controllerTests;

// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.test.context.ContextConfiguration;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.security.core.Authentication;

// import tqs.peticket.func.controller.AuthController;
// import tqs.peticket.func.repository.FuncRepository;
// import tqs.peticket.func.repository.UserRepository;
// import tqs.peticket.func.security.jwt.JwtUtils;
// import tqs.peticket.func.service.UserService;
// import tqs.peticket.func.model.User;
// import static org.mockito.ArgumentMatchers.any;

// @WebMvcTest(AuthController.class)
// @ContextConfiguration(classes = {AuthController.class, TestSecurityConfig.class})
// class AuthControllerTests {

//     @Autowired
//     private MockMvc mvc;

//     @MockBean
//     private UserRepository userRepository;

//     @MockBean
//     private JwtUtils jwtUtils;

//     @MockBean
//     private PasswordEncoder passwordEncoder;

//     @MockBean
//     private AuthenticationManager authenticationManager;

//     @MockBean
//     private UserService userService;

//     @MockBean
//     private FuncRepository funcRepository;

//     private User existingUser;
//     private User newUser;

//     @BeforeEach
//     public void setUp() {
//         existingUser = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");;
//         newUser = new User("Rodrigo", "Lopes", "rod@deti.com", "abcd1234", "Rua Direita, 374", "961284567");;
//     }

//     @Test
//     void testAuthenticateUser_Success() throws Exception {
//         when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(existingUser);
//         when(passwordEncoder.matches(existingUser.getPassword(), existingUser.getPassword())).thenReturn(true);
//         Authentication authentication = mock(Authentication.class);
//         when(authenticationManager.authenticate(any())).thenReturn(authentication);
//         when(jwtUtils.generateJwtToken(authentication)).thenReturn("mockedJWT");

//         mvc.perform(post("/api/auth/login")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(JsonUtils.toJson(existingUser)))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("mockedJWT"));
//     }

//     @Test
//     void testAuthenticateUser_InvalidPassword() throws Exception {
//         when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(existingUser);
//         when(passwordEncoder.matches(existingUser.getPassword(), existingUser.getPassword())).thenReturn(false);

//         mvc.perform(post("/api/auth/login")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(JsonUtils.toJson(existingUser)))
//                 .andExpect(status().isUnauthorized());
//     }

//     @Test
//     void testAuthenticateUser_UserNotFound() throws Exception {
//         when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(null);

//         mvc.perform(post("/api/auth/login")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(JsonUtils.toJson(existingUser)))
//                 .andExpect(status().isNotFound());
//     }

//     @Test
//     void testRegisterUser_Success() throws Exception {
//         when(userRepository.findByEmail(newUser.getEmail())).thenReturn(null);
//         when(passwordEncoder.encode(newUser.getPassword())).thenReturn(newUser.getPassword());

//         mvc.perform(post("/api/auth/register")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(JsonUtils.toJson(newUser)))
//                 .andExpect(status().isCreated());
//     }

//     @Test
//     void testRegisterUser_UserAlreadyExists() throws Exception {
//         when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(existingUser);

//         mvc.perform(post("/api/auth/register")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(JsonUtils.toJson(existingUser)))
//                 .andExpect(status().isConflict());
//     }
// }
