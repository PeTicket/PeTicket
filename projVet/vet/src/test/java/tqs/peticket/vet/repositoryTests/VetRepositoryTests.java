package tqs.peticket.vet.repositoryTests;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import tqs.peticket.vet.repository.VetRepository;
import tqs.peticket.vet.model.Vet;

@DataJpaTest(properties = {"spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"})
class VetRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VetRepository vetRepository;

    
    @Test
    void findByIdTest() {
        UUID userid = UUID.randomUUID();
        Vet vet = new Vet(userid);
        entityManager.persistAndFlush(vet);
        Vet found = vetRepository.findById(vet.getId());
        assertThat(found.getId()).isEqualTo(vet.getId());
    }

    @Test
    void deleteByIdTest() {
        UUID userid = UUID.randomUUID();
        Vet vet = new Vet(userid);
        entityManager.persistAndFlush(vet);
        vetRepository.deleteById(vet.getId());
        assertThat(vetRepository.existsById(vet.getId())).isFalse(); 
    }

    @Test
    void existsByIdTest() {
        UUID userid = UUID.randomUUID();
        Vet vet = new Vet(userid);
        entityManager.persistAndFlush(vet);
        boolean bool = vetRepository.existsById(vet.getId());
        assertThat(bool).isTrue(); 
    }

    @Test
    void findByUserIdTest() {
        UUID userid = UUID.randomUUID();
        Vet vet = new Vet(userid);
        entityManager.persistAndFlush(vet);
        Vet found = vetRepository.findByUserId(userid);
        assertThat(found.getId()).isEqualTo(vet.getId()); 
    }

    @Test
    void existsByUserIdTest() {
        UUID userid = UUID.randomUUID();
        Vet vet = new Vet(userid);
        entityManager.persistAndFlush(vet);
        boolean found = vetRepository.existsByUserId(userid);
        assertThat(found).isTrue();
    }
}
