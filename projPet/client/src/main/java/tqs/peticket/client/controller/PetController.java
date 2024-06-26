package tqs.peticket.client.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tqs.peticket.client.model.Pet;
import tqs.peticket.client.security.jwt.AuthHandler;
import tqs.peticket.client.service.PetService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/api/client/pet")
public class PetController {

    private static final Logger logger = LogManager.getLogger(PetController.class);
    
    @Autowired
    private PetService petService;
    
    @Autowired
    private AuthHandler authHandler;

    @GetMapping("/all")
    public ResponseEntity<List<Pet>> getAllPets() {
        logger.info("Getting all pets");
        List<Pet> pets = petService.getAllPets();
        if (pets == null || pets.isEmpty()) {
            logger.info("No pets found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(pets.size() + " pets found");
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/by-user-id")
    public ResponseEntity<List<Pet>> getPetsByUserId() {
        UUID userId = authHandler.getUserId();
        logger.info("Getting pets by user id " + userId);
        List<Pet> pets = petService.findByUserId(userId);
        if (pets == null || pets.isEmpty()) {
            logger.info("No pets found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(pets.size() + " pets found");
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("by-name/{name}")
    public ResponseEntity<List<Pet>> getPetsByName(@RequestParam String name) {
        UUID userId = authHandler.getUserId();
        logger.info("Getting pets by name " + name);
        List<Pet> pets = petService.findByName(name);
        if (pets.isEmpty()) {
            logger.info("No pets found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        if (!pets.get(0).getUserId().equals(userId)) {
            logger.info("Pet not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info(pets.size() + " pets found");
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<Pet> getPetById(@RequestParam UUID id) {
        UUID userId = authHandler.getUserId();
        logger.info("Getting pet by id " + id);
        Pet pet = petService.findById(id);
        if (pet == null) {
            logger.info("Pet not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!pet.getUserId().equals(userId)) {
            logger.info("Pet not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Pet found");
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Pet> addPet(@RequestBody Pet pet) {
        UUID userId = authHandler.getUserId();
        if (pet == null) {
            logger.info("Pet is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        pet.setUserId(userId);
        logger.info("Adding pet");
        petService.save(pet);
        return new ResponseEntity<>(pet, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Pet> updatePet(@RequestBody Pet pet) {
        UUID userId = authHandler.getUserId();
        if (pet == null) {
            logger.info("Pet is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!pet.getUserId().equals(userId)) {
            logger.info("User not authorized");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Pet updatedPet = petService.update(pet);
        if (updatedPet == null) {
            logger.info("Pet not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Pet updated");
        return new ResponseEntity<>(updatedPet, HttpStatus.OK);
    }
    

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Pet> deletePet(@PathVariable UUID id) {
        UUID userId = authHandler.getUserId();
        logger.info("Deleting pet with id: ", id);
        if (id == null) {
            logger.info("Pet id is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        logger.info("Deleting pet by id " + id);
        if (petService.findById(id) == null) {
            logger.info("Pet not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Pet pet = petService.findById(id);
        if (!pet.getUserId().equals(userId)) {
            logger.info("User not authorized");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        logger.info("Pet deleted");
        petService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
