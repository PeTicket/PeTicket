package tqs.peticket.vet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.annotation.PostConstruct;
import tqs.peticket.vet.model.User;
import tqs.peticket.vet.model.Vet;
import tqs.peticket.vet.repository.VetRepository;
import tqs.peticket.vet.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Autowired
    private VetRepository vetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        
            User existingUserOpt = userRepository.findByEmail("vet@example.com");

        if (existingUserOpt == null) {
            
            User user = new User("Bruno", "Tavares", "vet@example.com", "password", "123 Main St", "123-456-7890");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user = userRepository.save(user);

            Vet vet = new Vet(user.getId());
            vetRepository.save(vet);
        }
    }
}