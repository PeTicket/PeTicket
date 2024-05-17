package tqs.peticket.display.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import tqs.peticket.display.cache.Cache;


@Component
public class QueueConsumer {

    private final Cache cache;

    public QueueConsumer(Cache cache) {
        this.cache = cache;
    }

    @RabbitListener(queues = {"${queue.name}"}, containerFactory = "rabbitListenerContainerFactory")
    public void receive(@Payload String fileBody) {
        String[] parts = fileBody.split(" - ");
        if (parts.length == 2) {
            String key = parts[0];
            String value = parts[1];
            if (key.equals("clear")) {
                cache.clearCache();
                System.out.println("Cache cleared");
            } else {
                try {
                    cache.addToCache(key, Integer.parseInt(value));
                    System.out.println("Added to cache: " + key + " - " + value);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid value format: " + value);
                }
            }
        } else {
            System.out.println("Invalid message format: " + fileBody);
        }
    }

}