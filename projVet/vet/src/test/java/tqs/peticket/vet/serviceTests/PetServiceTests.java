package tqs.peticket.vet.serviceTests;

import tqs.peticket.vet.service.PetService;
import tqs.peticket.vet.model.Pet;
import tqs.peticket.vet.repository.PetRepository;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PetServiceTests {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    private Pet pet1;
    private Pet pet2;

    @BeforeEach
    void setUp() {
        pet1 = new Pet();
        pet1.setId(UUID.randomUUID());
        pet1.setName("Rex");
        pet1.setType("Dog");
        pet1.setBreed("Labrador");
        pet1.setAge("5");
        pet1.setWeight("20.0");
        pet1.setHeight("50.0");
        pet1.setBloodType("A+");
        pet1.setMedicalInfo("Healthy");
        pet1.setUserId(UUID.randomUUID());


        pet2 = new Pet();
        pet2.setId(UUID.randomUUID());
        pet2.setName("Mia");
        pet2.setType("Cat");
        pet2.setBreed("Siamese");
        pet2.setAge("3");
        pet2.setWeight("5.0");
        pet2.setHeight("30.0");
        pet2.setBloodType("B+");
        pet2.setMedicalInfo("Healthy");
        pet2.setUserId(UUID.randomUUID());

    }

     @Test
    @DisplayName("Test save")
    void testSave() {
        when(petRepository.existsById(pet1.getId())).thenReturn(false);
        when(petRepository.save(pet1)).thenReturn(pet1);
        Pet saved = petService.save(pet1);
        assertThat(saved).isEqualTo(pet1);
    }

    @Test
    @DisplayName("Test save existing pet")
    void testSaveExistingPet() {
        when(petRepository.existsById(pet1.getId())).thenReturn(true);
        Pet saved = petService.save(pet1);
        assertNull(saved);
    }

    @Test
    @DisplayName("Test findById")
    void testFindById() {
        when(petRepository.existsById(pet1.getId())).thenReturn(true);
        when(petRepository.findById(pet1.getId())).thenReturn(pet1);
        Pet found = petService.findById(pet1.getId());
        assertThat(found).isEqualTo(pet1);
    }

    @Test
    @DisplayName("Test findById not found")
    void testFindByIdNotFound() {
        when(petRepository.existsById(pet1.getId())).thenReturn(false);
        Pet found = petService.findById(pet1.getId());
        assertNull(found);
    }

    @Test
    @DisplayName("Test delete")
    void testDelete() {
        when(petRepository.existsById(pet1.getId())).thenReturn(true);
        petService.delete(pet1);
        verify(petRepository, times(1)).delete(pet1);
    }

    @Test
    @DisplayName("Test delete non-existing pet")
    void testDeleteNonExistingPet() {
        when(petRepository.existsById(pet1.getId())).thenReturn(false);
        petService.delete(pet1);
        verify(petRepository, never()).delete(pet1);
    }

    @Test
    @DisplayName("Test deleteById")
    void testDeleteById() {
        when(petRepository.existsById(pet1.getId())).thenReturn(true);
        petService.deleteById(pet1.getId());
        verify(petRepository, times(1)).deleteById(pet1.getId());
    }

    @Test
    @DisplayName("Test existsById")
    void testExistsById() {
        when(petRepository.existsById(pet1.getId())).thenReturn(true);
        Boolean exists = petService.existsById(pet1.getId());
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Test existsByUserId")
    void testExistsByUserId() {
        when(petRepository.existsByUserId(pet1.getUserId())).thenReturn(true);
        Boolean exists = petService.existsByUserId(pet1.getUserId());
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Test update")
    void testUpdate() {
        when(petRepository.existsById(pet1.getId())).thenReturn(true);
        when(petRepository.save(pet1)).thenReturn(pet1);
        Pet updatedPet = petService.update(pet1.getId(), pet1);
        assertThat(updatedPet).isEqualTo(pet1);
    }

    @Test
    @DisplayName("Test update non-existing pet")
    void testUpdateNonExistingPet() {
        when(petRepository.existsById(pet1.getId())).thenReturn(false);
        Pet updatedPet = petService.update(pet1.getId(), pet1);
        assertNull(updatedPet);
    }

    @Test
    @DisplayName("Test findByUserId")
    void testFindByUserId() {
        List<Pet> pets = new ArrayList<>();
        pets.add(pet1);
        when(petRepository.existsByUserId(pet1.getUserId())).thenReturn(true);
        when(petRepository.findByUserId(pet1.getUserId())).thenReturn(pets);
        List<Pet> foundPets = petService.findByUserId(pet1.getUserId());
        assertThat(foundPets).isEqualTo(pets);
    }

    @Test
    @DisplayName("Test findByUserId not found")
    void testFindByUserIdNotFound() {
        when(petRepository.existsByUserId(pet1.getUserId())).thenReturn(false);
        List<Pet> foundPets = petService.findByUserId(pet1.getUserId());
        assertNull(foundPets);
    }

    @Test
    @DisplayName("Test findByName")
    void testFindByName() {
        List<Pet> pets = new ArrayList<>();
        pets.add(pet1);
        when(petRepository.findByName("Rex")).thenReturn(pets);
        List<Pet> foundPets = petService.findByName("Rex");
        assertThat(foundPets).isEqualTo(pets);
    }

    @Test
    @DisplayName("Test findByName not found")
    void testFindByNameNotFound() {
        when(petRepository.findByName("Unknown")).thenReturn(new ArrayList<>());
        List<Pet> foundPets = petService.findByName("Unknown");
        assertNull(foundPets);
    }

    @Test
    @DisplayName("Test findByType")
    void testFindByType() {
        List<Pet> pets = new ArrayList<>();
        pets.add(pet1);
        when(petRepository.findByType("Dog")).thenReturn(pets);
        List<Pet> foundPets = petService.findByType("Dog");
        assertThat(foundPets).isEqualTo(pets);
    }

    @Test
    @DisplayName("Test findByType not found")
    void testFindByTypeNotFound() {
        when(petRepository.findByType("Unknown")).thenReturn(new ArrayList<>());
        List<Pet> foundPets = petService.findByType("Unknown");
        assertNull(foundPets);
    }

    @Test
    @DisplayName("Test findByBreed")
    void testFindByBreed() {
        List<Pet> pets = new ArrayList<>();
        pets.add(pet1);
        when(petRepository.findByBreed("Labrador")).thenReturn(pets);
        List<Pet> foundPets = petService.findByBreed("Labrador");
        assertThat(foundPets).isEqualTo(pets);
    }

    @Test
    @DisplayName("Test findByBreed not found")
    void testFindByBreedNotFound() {
        when(petRepository.findByBreed("Unknown")).thenReturn(new ArrayList<>());
        List<Pet> foundPets = petService.findByBreed("Unknown");
        assertNull(foundPets);
    }

    @Test
    @DisplayName("Test findByBloodType")
    void testFindByBloodType() {
        List<Pet> pets = new ArrayList<>();
        pets.add(pet1);
        when(petRepository.findByBloodType("A+")).thenReturn(pets);
        List<Pet> foundPets = petService.findByBloodType("A+");
        assertThat(foundPets).isEqualTo(pets);
    }

    @Test
    @DisplayName("Test findByBloodType not found")
    void testFindByBloodTypeNotFound() {
        when(petRepository.findByBloodType("Unknown")).thenReturn(new ArrayList<>());
        List<Pet> foundPets = petService.findByBloodType("Unknown");
        assertNull(foundPets);
    }

    @Test
    @DisplayName("Test deleteByUserId")
    void testDeleteByUserId() {
        when(petRepository.existsByUserId(pet1.getUserId())).thenReturn(true);
        petService.deleteByUserId(pet1.getUserId());
        verify(petRepository, times(1)).deleteByUserId(pet1.getUserId());
    }

    @Test
    @DisplayName("Test getAllPets")
    void testGetAllPets() {
        List<Pet> pets = new ArrayList<>();
        pets.add(pet1);
        pets.add(pet2);
        when(petRepository.findAll()).thenReturn(pets);
        List<Pet> allPets = petService.getAllPets();
        assertThat(allPets).isEqualTo(pets);
    }

    @Test
    @DisplayName("Test getAllPets empty")
    void testGetAllPetsEmpty() {
        when(petRepository.findAll()).thenReturn(new ArrayList<>());
        List<Pet> allPets = petService.getAllPets();
        assertNull(allPets);
    }

    @Test
    @DisplayName("Test getPetsByUserId")
    void testGetPetsByUserId() {
        List<Pet> pets = new ArrayList<>();
        pets.add(pet1);
        when(petRepository.findByUserId(pet1.getUserId())).thenReturn(pets);
        String petsString = petService.getPetsByUserId(pet1.getUserId().toString());
        assertThat(petsString).isEqualTo(pets.toString());
    }

    @Test
    @DisplayName("Test getPetsByName")
    void testGetPetsByName() {
        List<Pet> pets = new ArrayList<>();
        pets.add(pet1);
        when(petRepository.findByName("Rex")).thenReturn(pets);
        String petsString = petService.getPetsByName("Rex");
        assertThat(petsString).isEqualTo(pets.toString());
    }

    @Test
    @DisplayName("Test getPetById")
    void testGetPetById() {
        when(petRepository.findById(pet1.getId())).thenReturn(pet1);
        String petString = petService.getPetById(pet1.getId().toString());
        assertThat(petString).isEqualTo(pet1.toString());
    }
}