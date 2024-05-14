package tqs.peticket.client.repositoryTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.peticket.client.model.User;
import tqs.peticket.client.repository.UserRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void whenFindAlexByID_thenReturnUserAlex() {
        User alex = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(alex);
        User found = userRepository.findById(alex.getId());
        assertThat(found).isEqualTo(alex);
    }

    @Test
    void whenInvalidUserId_thenReturnNull() {
        UUID uuid = UUID.fromString("esteIDnaoexiste");
        User fromDb = userRepository.findById(uuid);
        assertThat(fromDb).isNull();
    }

    @Test
    void whenFindPedroByName_thenReturnUserPedro() {
        User pedro = new User("Pedro", "Lopes", "pdl@deti.com", "abcd1234", "Rua Esquerda, 274", "964434567");
        entityManager.persistAndFlush(pedro); 
        User found = userRepository.findByFirstNameAndLastName(pedro.getFirstName(), pedro.getLastName());
        assertThat(found).isEqualTo(pedro);
    }
    
}
