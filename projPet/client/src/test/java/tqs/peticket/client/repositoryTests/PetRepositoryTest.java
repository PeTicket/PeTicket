/*
package tqs.peticket.client.repositoryTests;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import tqs.peticket.client.repository.PetRepository;
import tqs.peticket.client.model.Pet;
import tqs.peticket.client.model.User;

@DataJpaTest
public class PetRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PetRepository petRepository;

    @Test
    void whenFindMaxByID_thenReturnPetMax() {
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        entityManager.persistAndFlush(max);
        Pet found = petRepository.findById(max.getId());
        assertThat(found).isEqualTo(max);
    }

    @Test
    void whenDeleteById_thenRemovePet() {
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet rick = new Pet(own.getId(), "Rick", "Lizzard", "Lizzard", "brown", "2"); 
        entityManager.persistAndFlush(rick);
        petRepository.deleteById(rick.getId());
        Pet deleted = petRepository.findById(rick.getId());
        assertThat(deleted).isNull();
    }

    @Test
    void whenExistsById_thenReturnTrue() {
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        entityManager.persistAndFlush(max);
        boolean exists = petRepository.existsById(max.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void whenFindByName_thenReturnPets() {
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        Pet uzi = new Pet(own.getId(), "Uzi", "cat", "Persian", "white", "3"); 
        Pet max2 = new Pet(own.getId(), "Max", "bird", "Parrot", "multi", "8");
        entityManager.persistAndFlush(max);
        entityManager.persistAndFlush(uzi);
        entityManager.persistAndFlush(max2);

        List<Pet> found = petRepository.findByName("Max");
        assertThat(found).hasSize(2);
        assertThat(found).containsExactlyInAnyOrder(max, max2);
    }

    @Test
    void whenFindByUserId_thenReturnPets() {
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        Pet bella = new Pet(own.getId(), "Bella", "cat", "Persian", "white", "3"); 
        entityManager.persistAndFlush(max);
        entityManager.persistAndFlush(bella);
        List<Pet> found = petRepository.findByUserId(own.getId());
        assertThat(found).hasSize(2);
        assertThat(found).containsExactlyInAnyOrder(max, bella);
    }

    @Test
    void whenFindByType_thenReturnPets() {
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        Pet bella = new Pet(own.getId(), "Bella", "dog", "Labrador", "black", "4"); 
        entityManager.persistAndFlush(max);
        entityManager.persistAndFlush(bella);
        List<Pet> found = petRepository.findByType("dog");
        assertThat(found).hasSize(2);
        assertThat(found).containsExactlyInAnyOrder(max, bella);
    }

    @Test
    void whenFindByBreed_thenReturnPets() {
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        Pet bella = new Pet(own.getId(), "Bella", "dog", "Golden Retriever", "black", "4"); 
        entityManager.persistAndFlush(max);
        entityManager.persistAndFlush(bella);
        List<Pet> found = petRepository.findByBreed("Golden Retriever");
        assertThat(found).hasSize(2);
        assertThat(found).containsExactlyInAnyOrder(max, bella);
    }

    @Test
    void whenFindByBloodType_thenReturnPets() {
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5", "30kg", "60cm", "A+", "Healthy"); 
        Pet bella = new Pet(own.getId(), "Bella", "dog", "Labrador", "black", "4", "25kg", "55cm", "A+", "Healthy"); 
        entityManager.persistAndFlush(max);
        entityManager.persistAndFlush(bella);
        List<Pet> found = petRepository.findByBloodType("A+");
        assertThat(found).hasSize(2);
        assertThat(found).containsExactlyInAnyOrder(max, bella);
    }

    @Test
    void whenExistsByUserId_thenReturnTrue() {
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        entityManager.persistAndFlush(max);
        assertThat(petRepository.existsByUserId(own.getId())).isTrue();
    }

    @Test
    void whenDeleteByUserId_thenRemovePets() {
        User own = new User("Alex", "Lopes", "alex@deti.com", "abcd1234", "Rua Direita, 274", "961234567");
        entityManager.persistAndFlush(own);
        Pet max = new Pet(own.getId(), "Max", "dog", "Golden Retriever", "golden", "5"); 
        Pet bella = new Pet(own.getId(), "Bella", "cat", "Persian", "white", "3"); 
        entityManager.persistAndFlush(max);
        entityManager.persistAndFlush(bella);
        petRepository.deleteByUserId(own.getId());
        entityManager.flush();
        List<Pet> found = petRepository.findByUserId(own.getId());
        assertThat(found).isEmpty();
    }

}
*/