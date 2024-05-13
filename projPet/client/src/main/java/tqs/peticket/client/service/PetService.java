package tqs.peticket.client.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqs.peticket.client.model.Pet;
import tqs.peticket.client.repository.PetRepository;

@Service
public class PetService {
    
    @Autowired
    private PetRepository petRepository;

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    public Pet findById(UUID id) {
        return petRepository.findById(id);
    }

    public void delete(Pet pet) {
        if (petRepository.existsById(pet.getId())) {
            petRepository.delete(pet);
        }
    }

    public void deleteById(UUID id) {
        if (petRepository.existsById(id)) {
            petRepository.deleteById(id);
        }
    }

    public Boolean existsById(UUID id) {
        return petRepository.existsById(id);
    }

    public Boolean existsByUserId(UUID userId) {
        return petRepository.existsByUserId(userId);
    }

    public Pet update(Pet pet) {
        if (petRepository.existsById(pet.getId())) {
            return petRepository.save(pet);
        } else {
            return null;
        }
    }

    public List<Pet> findByUserId(UUID userId) {
        return petRepository.findByUserId(userId);
    }

    public List<Pet> findByName(String name) {
        return petRepository.findByName(name);
    }

    public List<Pet> findByType(String type) {
        return petRepository.findByType(type);
    }

    public List<Pet> findByBreed(String breed) {
        return petRepository.findByBreed(breed);
    }

    public List<Pet> findByBloodType(String bloodType) {
        return petRepository.findByBloodType(bloodType);
    }

    public List<Pet> deleteByUserId(UUID userId) {
        if (petRepository.existsByUserId(userId)) {
            return petRepository.deleteByUserId(userId);
        } else {
            return null;
        }
    }

    public List<Pet> getAllPets() {
        if (petRepository.findAll().isEmpty()) {
            return null;
        }
        return petRepository.findAll();
    }

    public String getPetsByUserId(String userId) {
        return petRepository.findByUserId(UUID.fromString(userId)).toString();
    }

    public String getPetsByName(String name) {
        return petRepository.findByName(name).toString();
    }

    public String getPetById(String id) {
        return petRepository.findById(UUID.fromString(id)).toString();
    }

}
