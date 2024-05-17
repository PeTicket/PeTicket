package tqs.peticket.client.serviceTests;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tqs.peticket.client.security.jwt.AuthHandler;

@Configuration
public class Auth {
    @Bean
    public AuthHandler authHandler() {
        return Mockito.mock(AuthHandler.class);
    }
}
