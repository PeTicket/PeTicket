package tqs.peticket.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tqs.peticket.client.service.PetService;

@RestController
@RequestMapping("/api/client/pet")
public class PetController {
    
    @Autowired
    private PetService petService;
}
