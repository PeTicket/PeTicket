package tqs.peticket.func.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tqs.peticket.func.model.*;
import tqs.peticket.func.repository.UserRepository;
import tqs.peticket.func.service.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import java.util.List;

@RestController
@RequestMapping("/api/func")
public class FuncController {

    @Autowired
    private FuncService funcService;

      @Autowired
    UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private PetService petService;

    @Autowired
    private AppointmentService appointmentService;

    private static final Logger logger = LogManager.getLogger(FuncController.class);

    @PostMapping("/user/create")
    public ResponseEntity<User> createClient(@RequestBody User user) {
        logger.info("Creating client");
        User createdUser = userService.save(user);
        if (createdUser == null) {
            logger.info("Client not created");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(createdUser);
        logger.info("Client created");
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID userId) {
        logger.info("Deleting client with id " + userId);
        User user = userService.findById(userId);
        if (user == null) {
            logger.info("User not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.delete(user);
        logger.info("Client deleted");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<User> updateClient(@PathVariable UUID userId, @RequestBody User userDetails) {
        logger.info("Updating client with id " + userId);
        User user = userService.findById(userId);
        if (user == null) {
            logger.info("User not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User updatedUser = userService.update(userId, userDetails);  
        logger.info("Client updated");
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllClients() {
        logger.info("Fetching all clients");
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            logger.info("No clients found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(users.size() + " clients found");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getClient(@PathVariable UUID userId) {
        logger.info("Fetching client with id " + userId);
        User user = userService.findById(userId);
        if (user == null) {
            logger.info("Client not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Client found");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<User> getClientByEmail(@PathVariable String email) {
        logger.info("Fetching client with email " + email);
        User user = userService.findByEmail(email);
        if (user == null) {
            logger.info("Client not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Client found");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/pet/user/{userId}")
    public ResponseEntity<Pet> createPet(@PathVariable UUID userId, @RequestBody Pet pet) {
        logger.info("Creating pet for user with id " + userId);
        User user = userService.findById(userId);
        if (user == null) {
            logger.info("User not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UUID userID = user.getId();
        pet.setUserId(userID);
        Pet createdPet = petService.save(pet);
        if (createdPet == null) {
            logger.info("Pet not created");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Pet created");
        return new ResponseEntity<>(createdPet, HttpStatus.CREATED);
    }

    @GetMapping("/pets/users/{userId}")
    public ResponseEntity<List<Pet>> getPetsByUser(@PathVariable UUID userId) {
        logger.info("Fetching pets for user with id " + userId);
        List<Pet> pets = petService.findByUserId(userId);
        if (pets.isEmpty()) {
            logger.info("No pets found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(pets.size() + " pets found");
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<Pet> getPet(@PathVariable UUID petId) {
        logger.info("Fetching pet with id " + petId);
        Pet pet = petService.findById(petId);
        if (pet == null) {
            logger.info("Pet not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Pet found");
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @GetMapping("/pets")
    public ResponseEntity<List<Pet>> getAllPets() {
        logger.info("Fetching all pets");
        List<Pet> pets = petService.getAllPets();
        if (pets.isEmpty()) {
            logger.info("No pets found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(pets.size() + " pets found");
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @PutMapping("/pet/{petId}")
    public ResponseEntity<Pet> updatePet(@PathVariable UUID petId, @RequestBody Pet petDetails) {
        logger.info("Updating pet with id " + petId);
        Pet pet = petService.findById(petId);
        if (pet == null) {
            logger.info("Pet not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Pet updatedPet = petService.update(petId, petDetails);
        logger.info("Pet updated");
        return new ResponseEntity<>(updatedPet, HttpStatus.OK);
    }

    @DeleteMapping("/delete-pet/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable UUID petId) {
        logger.info("Deleting pet with id " + petId);
        Pet pet = petService.findById(petId);
        if (pet == null) {
            logger.info("Pet not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        petService.delete(pet);
        logger.info("Pet deleted");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    
}
