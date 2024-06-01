package tqs.peticket.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tqs.peticket.client.model.User;
import tqs.peticket.client.service.UserService;
import tqs.peticket.client.repository.UserRepository;
import tqs.peticket.client.security.services.UserDetailsImpl;
import tqs.peticket.client.security.jwt.JwtUtils;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody User user) {
        logger.info("Authenticating user " + user.getEmail());
        User userFound = userRepository.findByEmail(user.getEmail());
        if (userFound == null) {
            logger.info("User not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!passwordEncoder.matches(user.getPassword(), userFound.getPassword())) {
            logger.info("Invalid password");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        logger.info("User " + user.getEmail() + " authenticated");

        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        logger.info("Registering user " + user.getEmail());
        if (userRepository.findByEmail(user.getEmail()) != null) {
            logger.info("User already exists");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        logger.info("User " + user.getEmail() + " registered");

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
}
