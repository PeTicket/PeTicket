package tqs.peticket.client.serviceTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqs.peticket.client.service.PetService;
import tqs.peticket.client.model.Pet;
import tqs.peticket.client.repository.PetRepository;

import java.util.List;
import java.util.UUID;

@Service
public class PetServiceTest {

    @Autowired
    private PetRepository petRepository;

    public List<Pet> getAllPets() {
        if (petRepository.findAll().isEmpty()) {
            return null;
        }
        return petRepository.findAll();
    }

    public Pet save(Pet pet) {
        if (petRepository.existsById(pet.getId())) {
            return null;
        }
        return petRepository.save(pet);
    }

    public Pet findById(UUID id) {
        if (!petRepository.existsById(id)) {
            return null;
        }
        return petRepository.findById(id);
    }

    public void delete(Pet pet) {
        if (petRepository.existsById(pet.getId())) {
            petRepository.delete(pet);
        }
        else {
            return;
        }
    }

    public void deleteById(UUID id) {
        if (petRepository.existsById(id)) {
            petRepository.deleteById(id);
        }
        else {
            return;
        }
    }

    public Boolean existsById(UUID id) {
        return petRepository.existsById(id);
    }

    public Boolean existsByUserId(UUID userId) {
        return petRepository.existsByUserId(userId);
    }

    public Pet update(Pet pet) {
        Pet existingPet = petRepository.findById(pet.getId());
        if (existingPet != null) {
            existingPet.setName(pet.getName());
            existingPet.setType(pet.getType());
            existingPet.setBreed(pet.getBreed());
            existingPet.setColor(pet.getColor());
            existingPet.setAge(pet.getAge());
            existingPet.setMedicalInfo(pet.getMedicalInfo());
            return petRepository.save(existingPet);
        } else {
            return null;
        }
    }

    public List<Pet> findByUserId(UUID userId) {
        if (!petRepository.existsByUserId(userId)) {
            return null;
        }
        return petRepository.findByUserId(userId);
    }


    
}
