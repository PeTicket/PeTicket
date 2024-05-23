package tqs.peticket.client.repository;

import java.util.UUID;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tqs.peticket.client.model.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    Pet findById(UUID Id);
    void deleteById(UUID Id);
    Boolean existsById(UUID Id);
    List<Pet> findByName(String name);
    List<Pet> findByUserId(UUID userId);
    List<Pet> findByType(String type);
    List<Pet> findByBreed(String breed);
    List<Pet> findByBloodType(String bloodType);
    Boolean existsByUserId(UUID userId);
    void deleteByUserId(UUID userId);
    Boolean existsByIdAndUserId(UUID userId, UUID petId);
}
