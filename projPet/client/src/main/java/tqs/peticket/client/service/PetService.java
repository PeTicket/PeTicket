package tqs.peticket.client.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import jakarta.transaction.Transactional;
import tqs.peticket.client.model.Pet;
import tqs.peticket.client.repository.PetRepository;

@Service
public class PetService {
    
    @Autowired
    private PetRepository petRepository;

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
    @Transactional
    public void deleteById(UUID id) {

         if (id == null) {
            throw new IllegalArgumentException("Pet id is null");
         }
    
        if (petRepository.existsById(id)) {
            petRepository.deleteById(id);
        }
        else {
            throw new NotFoundException("Pet not found");
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
        if (!petRepository.existsByUserId(userId)) {
            return null;
        }
        return petRepository.findByUserId(userId);
    }

    public List<Pet> findByName(String name) {
        if (petRepository.findByName(name).isEmpty()) {
            return null;
        }
        return petRepository.findByName(name);
    }

    public List<Pet> findByType(String type) {
        if (petRepository.findByType(type).isEmpty()) {
            return null;
        }
        return petRepository.findByType(type);
    }

    public List<Pet> findByBreed(String breed) {
        if (petRepository.findByBreed(breed).isEmpty()) {
            return null;
        }
        return petRepository.findByBreed(breed);
    }

    public List<Pet> findByBloodType(String bloodType) {
        if (petRepository.findByBloodType(bloodType).isEmpty()) {
            return null;
        }
        return petRepository.findByBloodType(bloodType);
    }

    @Transactional
    public Void deleteByUserId(UUID userId) {
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
        if (userId == null) {
            return null;
        }
        return petRepository.findByUserId(UUID.fromString(userId)).toString();
    }

    public String getPetsByName(String name) {
        if (name == null) {
            return null;
        }
        return petRepository.findByName(name).toString();
    }

    public String getPetById(String id) {
        if (id == null) {
            return null;
        }
        return petRepository.findById(UUID.fromString(id)).toString();
    }

    public boolean existsPetByUserId(UUID userId, UUID petId) {
        return petRepository.existsByIdAndUserId(userId, petId);
    }

}
