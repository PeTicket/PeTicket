package tqs.peticket.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tqs.peticket.client.service.UserService;

import java.util.List;
import java.util.UUID;

import tqs.peticket.client.model.User;
import tqs.peticket.client.security.jwt.AuthHandler;

@RestController
@RequestMapping("/api/client/user")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;

    @Autowired
    private AuthHandler authHandler;

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
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        UUID userId = authHandler.getUserId();
        logger.info("Getting user by email " + email);
        User user = userService.findByEmail(email);
        if (user == null) {
            logger.info("No user found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        if (!user.getId().equals(userId)) {
            logger.info("Unauthorized access");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        logger.info("User found");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/by-id")
    public ResponseEntity<User> getUserById() {
        UUID id = authHandler.getUserId();
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
        UUID userId = authHandler.getUserId();
        user.setId(userId);
        logger.info("Saving user");
        User savedUser = userService.save(user);
        if (savedUser == null) {
            logger.info("User not saved");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("User saved");
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteUser() {
        UUID userId = authHandler.getUserId();
        logger.info("Deleting user");
        userService.deleteById(userId);
        logger.info("User deleted");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User userDetails) {
        UUID userId = authHandler.getUserId();
        logger.info("userDetails", userDetails);
        logger.info("Updating user with id " + userId);

        User existingUser = userService.findById(userId);

        if (existingUser == null) {
            logger.info("User not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        if (userDetails.getFirstName()== null) {
            logger.info("User Details:", userDetails);
            logger.info("User details null");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        logger.info("User Details:", userDetails.toString());

        existingUser.setFirstName(userDetails.getFirstName());
        existingUser.setLastName(userDetails.getLastName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setPassword(userDetails.getPassword());
        existingUser.setAddress(userDetails.getAddress());
        existingUser.setPhone(userDetails.getPhone());

        logger.info("User updated:", existingUser.toString());
        //store existing user id in a variable
        // UUID existingUser_id = existingUser.getId();
        // User updatedUser = userService.updateById(existingUser_id, existingUser)

        User updatedUser = userService.update(existingUser);
        if (updatedUser == null) {
            logger.info("User not updated");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("User updated");
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
