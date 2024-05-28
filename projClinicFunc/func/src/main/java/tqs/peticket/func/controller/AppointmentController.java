package tqs.peticket.func.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tqs.peticket.func.model.Appointment;
import tqs.peticket.func.service.AppointmentService;
import tqs.peticket.func.service.PetService;
import tqs.peticket.func.service.UserService;
import tqs.peticket.func.model.Pet;
import tqs.peticket.func.model.User;

import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/func/appointment")
public class AppointmentController {

    private static final Logger logger = LogManager.getLogger(AppointmentController.class);

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        logger.info("Fetching all appointments");
        List<Appointment> appointments = appointmentService.getAllAppointments();
        if (appointments.isEmpty()) {
            logger.info("No appointments found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(appointments.size() + " appointments found");
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable UUID id) {
        logger.info("Getting appointment by id " + id);
        Appointment appointment = appointmentService.findById(id);
        if (appointment == null) {
            logger.info("Appointment not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Appointment found");
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PostMapping("/appointment/{petId}/{userId}")
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

    @PutMapping("/update-prescription/{id}")
    public ResponseEntity<Appointment> updatePrescription(@PathVariable UUID id, @RequestBody String prescription) {
        logger.info("Updating prescription for appointment with id " + id);
        Appointment appointment = appointmentService.findById(id);
        if (appointment == null) {
            logger.info("Appointment not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        appointment.setPrescription(prescription);
        Appointment updatedAppointment = appointmentService.save(appointment);
        logger.info("Prescription updated");
        return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
    }

    @PostMapping("/next")
    public ResponseEntity<Appointment> getNextAppointment() {
        logger.info("Getting next appointment");
        Appointment appointment = appointmentService.findNextAppointment();
        if (appointment == null) {
            logger.info("No appointments found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info("Next appointment found");
        appointment.setStatus("in_progress");
        appointmentService.save(appointment);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @DeleteMapping("/appointment/{appointmentId}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable UUID appointmentId) {
        logger.info("Deleting appointment with id " + appointmentId);
        Appointment appointment = appointmentService.findById(appointmentId);
        if (appointment == null) {
            logger.info("Appointment not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        appointmentService.delete(appointment);
        logger.info("Appointment deleted");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/appointmentQrCode/{appointmentId}")
    public ResponseEntity<Appointment> updateQrCode(@PathVariable UUID appointmentId, @RequestBody String statusQrCode) {
        logger.info("Updating qr code for appointment with id " + appointmentId);
        Appointment appointment = appointmentService.findById(appointmentId);
        if (appointment == null) {
            logger.info("Appointment not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        appointment.setStatus(statusQrCode);
        Appointment updatedAppointment = appointmentService.save(appointment);
        logger.info("Qr code updated");
        return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
    }
}
















