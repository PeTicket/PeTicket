package tqs.peticket.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tqs.peticket.client.model.Appointment;
import tqs.peticket.client.service.AppointmentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    @GetMapping("/by-user-id/{userId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByUserId(@RequestParam UUID userId) {
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
        logger.info("Getting appointments by pet id " + petId);
        List<Appointment> appointments = appointmentService.findByPetId(petId);
        if (appointments.isEmpty()) {
            logger.info("No appointments found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(appointments.size() + " appointments found");
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/by-vet-id/{vetId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByVetId(@RequestParam UUID vetId) {
        logger.info("Getting appointments by vet id " + vetId);
        List<Appointment> appointments = appointmentService.findByVetId(vetId);
        if (appointments.isEmpty()) {
            logger.info("No appointments found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(appointments.size() + " appointments found");
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@RequestParam UUID id) {
        logger.info("Getting appointment by id " + id);
        Appointment appointment = appointmentService.findById(id);
        if (appointment == null) {
            logger.info("Appointment not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Appointment found");
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PostMapping("/add/{dateString}/{timeString}")
    public ResponseEntity<Appointment> addAppointment(@RequestBody Appointment appointment,@PathVariable String dateString, @PathVariable String timeString) {
        if (appointment == null) {
            logger.info("Appointment is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

        LocalDate date = LocalDate.parse(dateString,formatter);
        LocalDateTime time =LocalDateTime.parse(timeString);

        appointment.setDate(date);
        appointment.setTime(time);

        logger.info("Adding appointment");
        appointmentService.save(appointment);
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Appointment> updateAppointment(@RequestBody Appointment appointment) {
        if (appointment == null) {
            logger.info("Appointment is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Updating appointment");
        appointmentService.update(appointment);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Appointment> deleteAppointment(@RequestParam UUID id) {
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
