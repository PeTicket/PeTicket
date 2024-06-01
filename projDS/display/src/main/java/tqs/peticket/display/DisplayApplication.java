package tqs.peticket.display;

import org.springframework.boot.SpringApplication;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class DisplayApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(DisplayApplication.class, args);
	}

}
