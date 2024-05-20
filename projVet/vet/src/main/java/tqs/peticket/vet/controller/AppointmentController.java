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
import tqs.peticket.vet.security.jwt.AuthHandler;
import tqs.peticket.vet.service.AppointmentService;
import tqs.peticket.vet.service.PetService;
import tqs.peticket.vet.service.UserService;
import tqs.peticket.vet.service.VetService;

@RestController
@RequestMapping("/api/vet/appointment")
public class AppointmentController {

    private static final Logger logger = LogManager.getLogger(AppointmentController.class);

    @Autowired
    private AppointmentService appointmentService;

    @Autowired 
    private AuthHandler authHandler;

    @Autowired
    private UserService userService;
    
    @Autowired
    private VetService vetService;

    @Autowired  
    private PetService petService;

    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> getTodaysAppointments() {
        logger.info("GET /api/vet/appointment/all");
        if (!authHandler.isVet()) {
            logger.error("GET /api/vet/appointment/all 403");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<Appointment> appointments = appointmentService.findAll();
        logger.info("GET /api/vet/appointment/all 200");
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

}