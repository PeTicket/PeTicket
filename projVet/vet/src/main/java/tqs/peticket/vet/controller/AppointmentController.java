package tqs.peticket.vet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tqs.peticket.vet.model.Appointment;
import tqs.peticket.vet.security.jwt.AuthHandler;
import tqs.peticket.vet.service.AppointmentService;

import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@RestController
@RequestMapping("/api/vet/appointment")
public class AppointmentController {

    private static final Logger logger = LogManager.getLogger(AppointmentController.class);

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AuthHandler authHandler;

    @GetMapping
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

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable UUID id, @RequestBody Appointment appointmentDetails) {
        logger.info("Updating appointment with id " + id);
        Appointment existingAppointment = appointmentService.findById(id);
        if (existingAppointment == null) {
            logger.info("Appointment not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UUID loggedInVetId = authHandler.getUserId();
        if (!loggedInVetId.equals(existingAppointment.getVetId())) {
            logger.info("Unauthorized access by vet " + loggedInVetId);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        appointmentDetails.setVetId(loggedInVetId);
        Appointment updatedAppointment = appointmentService.update(id, appointmentDetails);
        if (updatedAppointment == null) {
            logger.info("Appointment not updated");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Appointment updated");
        return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable UUID id) {
        logger.info("Deleting appointment with id " + id);
        Appointment appointment = appointmentService.findById(id);
        if (appointment == null) {
            logger.info("Appointment not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UUID loggedInVetId = authHandler.getUserId();
        if (!loggedInVetId.equals(appointment.getVetId())) {
            logger.info("Unauthorized access by vet " + loggedInVetId);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        appointmentService.deleteById(id);
        logger.info("Appointment deleted");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/prescription")
    public ResponseEntity<Appointment> updatePrescription(@PathVariable UUID id, @PathVariable String prescription) {
        Appointment appointment = appointmentService.findById(id);
        logger.info("Updating prescription for appointment with id " + id);
        if (appointment == null) {
            logger.info("Appointment not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UUID loggedInVetId = authHandler.getUserId();
        if (!loggedInVetId.equals(appointment.getVetId())) {
            logger.info("Unauthorized access by vet " + loggedInVetId);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        appointment.setPrescription(prescription);
        Appointment updatedAppointment = appointmentService.save(appointment);
        if (updatedAppointment == null) {
            logger.info("Prescription not updated");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Prescription updated");
        return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
    }
}
