package tqs.peticket.func.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tqs.peticket.func.model.*;
import tqs.peticket.func.service.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

@RestController
@RequestMapping("/api/func")
public class FuncController {

    @Autowired
    private FuncService funcService;

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    @Autowired
    private AppointmentService appointmentService;

    private static final Logger logger = LogManager.getLogger(FuncController.class);

    @PostMapping("/create-client")
    public ResponseEntity<User> createClient(@RequestBody User user) {
        logger.info("Creating client");
        User createdUser = userService.save(user);
        if (createdUser == null) {
            logger.info("Client not created");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Client created");
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/create-pet/{userId}")
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

    @PostMapping("/create-appointment/{petId}/{userId}")
    public ResponseEntity<Appointment> createAppointment(@PathVariable UUID petId, @PathVariable UUID userId, @RequestBody Appointment appointment) {
        logger.info("Creating appointment for pet with id " + petId + " and user with id " + userId);
        Pet pet = petService.findById(petId);
        User user = userService.findById(userId);
        if (pet == null || user == null) {
            logger.info("Pet or User not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        appointment.setPetId(petId);
        appointment.setUserId(userId);
        Appointment createdAppointment = appointmentService.save(appointment);
        if (createdAppointment == null) {
            logger.info("Appointment not created");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Appointment created");
        return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
    }
}
