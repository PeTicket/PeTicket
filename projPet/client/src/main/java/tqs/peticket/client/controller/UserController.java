package tqs.peticket.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tqs.peticket.client.service.UserService;

import java.util.List;
import java.util.UUID;

import tqs.peticket.client.model.User;

@RestController
@RequestMapping("/api/client/user")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("Getting all users");
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            logger.info("No users found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(users.size() + " users found");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        logger.info("Getting user by email " + email);
        User user = userService.findByEmail(email);
        if (user == null) {
            logger.info("No user found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info("User found");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<User> getUserById(@RequestParam UUID id) {
        logger.info("Getting user by id " + id);
        User user = userService.findById(id);
        if (user == null) {
            logger.info("No user found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info("User found");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        logger.info("Saving user");
        User savedUser = userService.save(user);
        if (savedUser == null) {
            logger.info("User not saved");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("User saved");
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> deleteUser(@RequestBody User user) {
        logger.info("Deleting user");
        userService.delete(user);
        logger.info("User deleted");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
