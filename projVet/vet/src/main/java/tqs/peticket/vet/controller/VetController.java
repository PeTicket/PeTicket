package tqs.peticket.vet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tqs.peticket.vet.model.Pet;
import tqs.peticket.vet.model.User;
import tqs.peticket.vet.service.PetService;
import tqs.peticket.vet.service.UserService;

import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@RestController
@RequestMapping("/api/vet")
public class VetController {

    private static final Logger logger = LogManager.getLogger(VetController.class);
    
    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    @GetMapping("/users")
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

    @GetMapping("/users/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        logger.info("Getting user by email " + email);
        User user = userService.findByEmail(email);
        if (user == null) {
            logger.info("No user found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info("User found");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/users/by-id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        logger.info("Getting user by id " + id);
        User user = userService.findById(id);
        if (user == null) {
            logger.info("No user found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info("User found");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/pets")
    public ResponseEntity<List<Pet>> getAllPets() {
        logger.info("Getting all pets");
        List<Pet> pets = petService.getAllPets();
        if (pets.isEmpty()) {
            logger.info("No pets found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(pets.size() + " pets found");
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/pets/user/{userId}")
    public ResponseEntity<List<Pet>> getPetsByUserId(@PathVariable UUID userId) {
        logger.info("Getting pets by user id " + userId);
        List<Pet> pets = petService.findByUserId(userId);
        if (pets.isEmpty()) {
            logger.info("No pets found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(pets.size() + " pets found");
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/pets/name/{name}")
    public ResponseEntity<List<Pet>> getPetsByName(@PathVariable String name) {
        logger.info("Getting pets by name " + name);
        List<Pet> pets = petService.findByName(name);
        if (pets.isEmpty()) {
            logger.info("No pets found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(pets.size() + " pets found");
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/pets/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable UUID id) {
        logger.info("Getting pet by id " + id);
        Pet pet = petService.findById(id);
        if (pet == null) {
            logger.info("Pet not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Pet found");
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @PutMapping("/pets/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable UUID id, @RequestBody Pet pet) {
        logger.info("Updating pet with id " + id);
        Pet updatedPet = petService.update(id, pet);
        if (updatedPet == null) {
            logger.info("Pet not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Pet updated");
        return new ResponseEntity<>(updatedPet, HttpStatus.OK);
    }
}
