package tqs.peticket.vet.repositoryTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.peticket.vet.model.User;
import tqs.peticket.vet.repository.UserRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {"spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"})
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
        UUID uuid = UUID.fromString("bbcc4621-d88f-4a94-ae2f-b38072bf5087");
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

    @Test
    void whenDeleteById_thenIsDeleted(){
        User user1 = new User("User", "One", "usr@deti.com", "abcd1234", "Aveiro, 274", "966634567");
        entityManager.persistAndFlush(user1);
        userRepository.deleteById(user1.getId());
        User del_user = userRepository.findById(user1.getId());
        assertThat(del_user).isNull();
    }

    @Test
    void whenFindTitaByEmail_thenReturnUserTita(){
        User tita = new User("Tita", "Ferrei", "tita@deti.com", "abcd1234", "Aveiro, 274", "967734567");
        entityManager.persistAndFlush(tita);
        User found = userRepository.findByEmail(tita.getEmail());
        assertThat(found).isEqualTo(tita);
    }

    @Test
    void whenCheckIfPedroExistsById_thenReturnTrue(){
        User pedro = new User("Pedro", "Almeida", "pdalm@deti.com", "abcd1234", "Example, 225", "914434567");
        entityManager.persistAndFlush(pedro);
        assertThat(userRepository.existsById(pedro.getId())).isTrue(); 
    }

    @Test
    void whenCheckIfRuiExistsByEmail_thenReturnTrue(){
        User rui = new User("Rui", "Pedro", "rp@deti.com", "ababcdcd", "Casa Minha", "232565848");
        entityManager.persistAndFlush(rui);
        assertThat(userRepository.existsByEmail(rui.getEmail())).isTrue();
    }

    @Test
    void whenFindJoeByPhone_thenReturnUserJoe(){
        User joe = new User("Joe", "Pedro", "joe@deti.com", "ababcdcd", "Casa Tua", "232865848");
        entityManager.persistAndFlush(joe);
        User found = userRepository.findByPhone(joe.getPhone());
        assertThat(found).isEqualTo(joe);
    }
    
}