package tqs.peticket.vet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.amqp.core.AmqpTemplate;

import tqs.peticket.vet.middleware.SenderConfig;

@RestController
@RequestMapping("/api")
public class TesteController {

    private final SenderConfig senderConfig;

    @Autowired
    public TesteController(SenderConfig senderConfig) {
        this.senderConfig = senderConfig;
    }
    
    @PostMapping("/teste")    
    public String sendTestMessage() {
        System.out.println("Sending message...");
        senderConfig.send("C02 - 15");
        return "Message sent!";
    }

}