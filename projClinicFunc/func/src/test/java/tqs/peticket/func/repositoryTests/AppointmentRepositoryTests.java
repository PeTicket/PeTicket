package tqs.peticket.func.repositoryTests;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.peticket.func.repository.AppointmentRepository;
import tqs.peticket.func.model.Appointment;
import tqs.peticket.func.model.Pet;
import tqs.peticket.func.model.User;

@DataJpaTest(properties = {"spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"})
class AppointmentRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AppointmentRepository aptmRepository;

    @Test
    public void testfindById() {
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        entityManager.persistAndFlush(max);
        Appointment aptm1 = new Appointment(own.getId(), max.getId(), "2021-06-01", "10:00", "Incontinencia", "scheduled");
        entityManager.persistAndFlush(aptm1);
        Appointment found = aptmRepository.findById(aptm1.getId());
        assertThat(found).isEqualTo(aptm1);
    }

    @Test
    public void deleteById() {
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        entityManager.persistAndFlush(max);
        Appointment aptm1 = new Appointment(own.getId(), max.getId(), "2021-06-01", "10:00", "Incontinencia", "scheduled");
        entityManager.persistAndFlush(aptm1);
        aptmRepository.deleteById(aptm1.getId());
        Appointment deleted = aptmRepository.findById(aptm1.getId());
        assertThat(deleted).isNull();

    }

    @Test
    public void existsById() {
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        entityManager.persistAndFlush(max);
        Appointment aptm1 = new Appointment(own.getId(), max.getId(), "2021-06-01", "10:00", "Incontinencia", "scheduled");
        entityManager.persistAndFlush(aptm1);
        boolean exists = aptmRepository.existsById(aptm1.getId());
        assertThat(exists).isTrue();
    }

    @Test
    public void existsByUserId() {
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        entityManager.persistAndFlush(max);
        Appointment aptm1 = new Appointment(own.getId(), max.getId(), "2021-06-01", "10:00", "Incontinencia", "scheduled");
        entityManager.persistAndFlush(aptm1);
        boolean exists = aptmRepository.existsByUserId(own.getId());
        assertThat(exists).isTrue();
    }

    @Test
    public void existsByPetId(){
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        entityManager.persistAndFlush(max);
        Appointment aptm1 = new Appointment(own.getId(), max.getId(), "2021-06-01", "10:00", "Incontinencia", "scheduled");
        entityManager.persistAndFlush(aptm1);
        boolean exists = aptmRepository.existsByPetId(max.getId());
        assertThat(exists).isTrue();
    }

    @Test
    public void existsByVetId() {
        UUID vetId = UUID.randomUUID();
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        entityManager.persistAndFlush(max);
        Appointment aptm1 = new Appointment(own.getId(), max.getId(), "2021-06-01", "10:00", "Incontinencia", "scheduled");
        aptm1.setVetId(vetId);
        entityManager.persistAndFlush(aptm1);
        boolean exists = aptmRepository.existsByVetId(vetId);
        assertThat(exists).isTrue();
    }

    @Test
    public void findByUserId() {
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        entityManager.persistAndFlush(max);
        Pet rick = new Pet(own.getId(), "Rick", "Lizzard", "Lizzard", "brown", "2");
        entityManager.persistAndFlush(rick);
        Appointment aptm1 = new Appointment(own.getId(), max.getId(), "2021-06-01", "10:00", "Incontinencia", "scheduled");
        entityManager.persistAndFlush(aptm1);
        Appointment aptm2 = new Appointment(own.getId(), rick.getId(), "2021-06-01", "15:00", "Blind", "scheduled");
        entityManager.persistAndFlush(aptm2);
        List<Appointment> found = aptmRepository.findByUserId(own.getId());
        assertThat(found).hasSize(2);
    }

    @Test
    public void findByPetId(){
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        entityManager.persistAndFlush(max);
        Appointment aptm1 = new Appointment(own.getId(), max.getId(), "2021-06-01", "10:00", "Incontinencia", "scheduled");
        entityManager.persistAndFlush(aptm1);
        List<Appointment> found = aptmRepository.findByPetId(max.getId());
        assertThat(found).hasSize(1);
    }

    @Test
    public void findByVetId() {
        UUID vetId = UUID.randomUUID();
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        entityManager.persistAndFlush(max);
        Appointment aptm1 = new Appointment(own.getId(), max.getId(), "2021-06-01", "10:00", "Incontinencia", "scheduled");
        aptm1.setVetId(vetId);
        entityManager.persistAndFlush(aptm1);
        List<Appointment> found = aptmRepository.findByVetId(vetId);
        assertThat(found).hasSize(1);
    }

    @Test
    public void findByStatus(){
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        entityManager.persistAndFlush(max);
        Appointment aptm1 = new Appointment(own.getId(), max.getId(), "2021-06-01", "10:00", "Incontinencia", "scheduled");
        entityManager.persistAndFlush(aptm1);
        List<Appointment> found = aptmRepository.findByStatus("scheduled");
        assertThat(found).hasSize(1);
    }

    @Test
    public void findByDateAndTime(){
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        entityManager.persistAndFlush(max);
        Appointment aptm1 = new Appointment(own.getId(), max.getId(), "2021-06-01", "10:00", "Incontinencia", "scheduled");
        entityManager.persistAndFlush(aptm1);
        List<Appointment> found = aptmRepository.findByDateAndTime("2021-06-01", "10:00");
        assertThat(found).hasSize(1);
    }

    @Test
    public void findByDate(){
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        entityManager.persistAndFlush(max);
        Appointment aptm1 = new Appointment(own.getId(), max.getId(), "2021-06-01", "10:00", "Incontinencia", "scheduled");
        entityManager.persistAndFlush(aptm1);
        List<Appointment> found = aptmRepository.findByDate("2021-06-01");
        assertThat(found).hasSize(1);
    }

    @Test
    public void findByTime(){
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        entityManager.persistAndFlush(max);
        Appointment aptm1 = new Appointment(own.getId(), max.getId(), "2021-06-01", "10:00", "Incontinencia", "scheduled");
        entityManager.persistAndFlush(aptm1);
        List<Appointment> found = aptmRepository.findByTime("10:00");
        assertThat(found).hasSize(1);
    }

}