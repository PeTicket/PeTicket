package tqs.peticket.func.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.annotation.PostConstruct;
import tqs.peticket.func.model.Func;
import tqs.peticket.func.model.User;
import tqs.peticket.func.repository.FuncRepository;
import tqs.peticket.func.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Autowired
    private FuncRepository funcRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        
            User existingUserOpt = userRepository.findByEmail("funcionario@example.com");

        if (existingUserOpt == null) {
            
            User user = new User("Amelia", "Jacinto", "funcionario@example.com", "password", "123 Main St", "123-456-7890");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user = userRepository.save(user);

            Func func = new Func(user.getId());
            funcRepository.save(func);
        }
    }
}