package tqs.peticket.display.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
public class QueueConsumer {

    @RabbitListener(queues = {"${queue.name}"}, containerFactory = "rabbitListenerContainerFactory")
    public void receive(@Payload String fileBody) {
        System.out.println("Message " + fileBody);

    }

}