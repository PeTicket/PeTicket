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
import org.springframework.stereotype.Component;


@Component
public class SenderConfig {

    private final RabbitTemplate rabbitTemplate;

    private final String queueName;

    public SenderConfig(RabbitTemplate rabbitTemplate, @Value("${queue.name}") String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    public void send(String message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }
}