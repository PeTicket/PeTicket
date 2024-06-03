package tqs.peticket.display.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod; // Import the necessary class
import static org.springframework.security.config.Customizer.withDefaults; // Static import for withDefaults

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers(HttpMethod.GET, "/**").permitAll() // Allow GET requests to all paths
                    .requestMatchers("/**").permitAll() // Allow all other requests to all paths
                    .anyRequest().authenticated() // Require authentication for any other requests
            )
            .csrf().disable() // Disable CSRF protection for simplicity (consider enabling it in production)
            .httpBasic(withDefaults()); // Use HTTP Basic authentication (optional, depending on your authentication mechanism)

        return http.build();
    }
}
