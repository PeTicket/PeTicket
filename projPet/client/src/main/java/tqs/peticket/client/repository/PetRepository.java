package tqs.peticket.client.repository;

import java.util.UUID;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tqs.peticket.client.model.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {
    Pet findById(UUID Id);
    Pet deleteById(UUID Id);
    Boolean existsById(UUID Id);
    List<Pet> findByName(String name);
    List<Pet> findByUserId(UUID userId);
    List<Pet> findByType(String type);
    List<Pet> findByBreed(String breed);
    List<Pet> findByBloodType(String bloodType);
    Boolean existsByUserId(UUID userId);
    List<Pet> deleteByUserId(UUID userId);
}
