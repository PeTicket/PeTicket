package tqs.peticket.vet.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import tqs.peticket.vet.model.Appointment;
import tqs.peticket.vet.service.AppointmentService;

@RestController
@RequestMapping("/api/vet/appointment")
public class AppointmentController {

    private static final Logger logger = LogManager.getLogger(AppointmentController.class);

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> getTodaysAppointments() {
        logger.info("Getting today's appointments");
        LocalDate currentDate = LocalDate.now();
        List<Appointment> appointments = appointmentService.findByDate(currentDate);
        if (appointments.isEmpty()) {
            logger.info("No appointments found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info(appointments.size() + " appointments found");
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }
}