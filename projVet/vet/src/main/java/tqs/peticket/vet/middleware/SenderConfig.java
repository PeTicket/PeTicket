package tqs.peticket.vet.middleware;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class SenderConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${queue.name}")
    private String queueName;

    public void send(String message) {
        rabbitTemplate.convertAndSend("direct-exchange", "routing-key-teste", message);
    }
}
