package tqs.peticket.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.WriterException;

import jakarta.security.auth.message.config.AuthConfig;
import tqs.peticket.client.model.Appointment;
import tqs.peticket.client.security.jwt.AuthHandler;
import tqs.peticket.client.service.AppointmentService;
import tqs.peticket.client.service.PetService;
import tqs.peticket.client.service.QrCodeService;
import tqs.peticket.client.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/client/appointment")
public class AppointmentController {

    private static final Logger logger = LogManager.getLogger(AppointmentController.class);

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    @Autowired
    private AuthHandler authHandler;

    @Autowired
    private QrCodeService qrCodeService;

    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        logger.info("Getting all appointments");
        List<Appointment> appointments = appointmentService.getAllAppointments();
        if (appointments.isEmpty()) {
            logger.info("No appointments found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(appointments.size() + " appointments found");
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/by-user-id")
    public ResponseEntity<List<Appointment>> getAppointmentsByUserId() {
        UUID userId = authHandler.getUserId();
        logger.info("Getting appointments by user id " + userId);
        List<Appointment> appointments = appointmentService.findByUserId(userId);
        if (appointments.isEmpty()) {
            logger.info("No appointments found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(appointments.size() + " appointments found");
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/by-pet-id/{petId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPetId(@RequestParam UUID petId) {
        UUID userId = authHandler.getUserId();
        if (!appointmentService.existsByPetId(petId)) {
            logger.info("Pet not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        if (!appointmentService.existsByUserId(userId)) {
            logger.info("User not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!appointmentService.existsPetByUserId(userId, petId)) {
            logger.info("Pet not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("Getting appointments by pet id " + petId);
        List<Appointment> appointments = appointmentService.findByPetId(petId);
        if (appointments.isEmpty()) {
            logger.info("No appointments found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(appointments.size() + " appointments found");
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }


    @GetMapping("/by-id/{id}")
    public ResponseEntity<Appointment> getAppointmentById( @RequestParam UUID id) {
        UUID Userid = authHandler.getUserId();
        logger.info("Getting appointment by id " + id);
        Appointment appointment = appointmentService.findById(id);
        // comparar se o userid do token é igual ao userid do appointment
        if (appointment == null) {
            logger.info("Appointment not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (Userid != appointment.getUserId()) {
            logger.info("User not authorized");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        logger.info("Appointment found");
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addAppointment(@RequestBody Appointment appointment) throws WriterException, IOException {
        UUID userId = authHandler.getUserId();
        System.out.println(userId);
        // comparar se o userid do token é igual ao userid do appointment
        if (appointment == null) {
            logger.info("Appointment is null");
            String errorMessage = "Appointment is null";
            return ResponseEntity.badRequest().body(errorMessage);
        }
        
        if (!petService.existsById(appointment.getPetId())) {
            logger.info("User not found");
            String errorMessage = "User not found";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
        
        // if (!petService.existsPetByUserId(userId, appointment.getPetId())) {
        //     logger.info("Pet not found");
        //     String errorMessage = "Pet not found";
        //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        // }
        appointment.setUserId(userId);

        Byte[] img = qrCodeService.generateQRCodeImage(userId, appointment.getPetId(), appointment.getId());

        appointment.setQrCode(img);
        logger.info("Adding appointment");
        appointmentService.save(appointment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Appointment> updateAppointment(@RequestBody Appointment appointment) {
        UUID userId = authHandler.getUserId();
        if (appointment == null) {
            logger.info("Appointment is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!appointmentService.existsByUserId(userId)) {
            logger.info("User not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!appointmentService.existsByPetId(appointment.getPetId())) {
            logger.info("Pet not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!appointmentService.existsPetByUserId(userId, appointment.getPetId())) {
            logger.info("Pet not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (appointmentService.findById(appointment.getId()) == null) {
            logger.info("Appointment not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (userId != appointment.getUserId()) {
            logger.info("User not authorized");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        logger.info("Updating appointment");
        appointmentService.update(appointment);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Appointment> deleteAppointment(@PathVariable UUID id) {
        logger.info("Deleting appointment by id " + id);
        Appointment appointment = appointmentService.findById(id);
        if (appointment == null) {
            logger.info("Appointment not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Deleting appointment");
        appointmentService.delete(appointment);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
