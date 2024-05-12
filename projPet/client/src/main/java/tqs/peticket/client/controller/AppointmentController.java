package tqs.peticket.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tqs.peticket.client.service.AppointmentService;

@RestController
@RequestMapping("/api/client/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

}
