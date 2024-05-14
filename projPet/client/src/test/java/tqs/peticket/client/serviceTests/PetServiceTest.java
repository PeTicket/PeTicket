package tqs.peticket.client.serviceTests;

import tqs.peticket.client.service.PetService;
import tqs.peticket.client.model.Pet;
import tqs.peticket.client.repository.PetRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

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
    @DisplayName("Test delete")
    void testDelete() {
        when(petRepository.existsById(pet1.getId())).thenReturn(true);
        petService.delete(pet1);
        verify(petRepository, times(1)).delete(pet1);
    }

    @Test
    @DisplayName("Test deleteById")
    void testDeleteById() {
        when(petRepository.existsById(pet1.getId())).thenReturn(true);
        doNothing().when(petRepository).deleteById(pet1.getId());
        petService.deleteById(pet1.getId());
        verify(petRepository, times(1)).deleteById(pet1.getId());
    }

    @Test
    @DisplayName("Test update")
    void testUpdate() {
        when(petRepository.existsById(pet1.getId())).thenReturn(true);
        when(petRepository.save(pet1)).thenReturn(pet1);
        Pet updatedPet = petService.update(pet1);
        assertThat(updatedPet).isEqualTo(pet1);
    }


    
}
