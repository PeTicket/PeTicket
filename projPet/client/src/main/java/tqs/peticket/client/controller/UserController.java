package tqs.peticket.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tqs.peticket.client.service.UserService;

@RestController
@RequestMapping("/api/client/user")
public class UserController {
    
    @Autowired
    private UserService userService;
}
