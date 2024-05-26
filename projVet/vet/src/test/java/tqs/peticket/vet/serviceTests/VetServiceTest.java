package tqs.peticket.vet.serviceTests;

import tqs.peticket.vet.service.VetService;
import tqs.peticket.vet.model.Vet;
import tqs.peticket.vet.repository.VetRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VetServiceTest {

    @Mock
    private VetRepository vetRepository;

    @InjectMocks
    private VetService vetService;

    private Vet vet1;
    private Vet vet2;

    @BeforeEach
    void setUp() {
        vet1 = new Vet(UUID.randomUUID());
        vet1.setId(UUID.randomUUID());
        vet1.setRoomId("Room1");

        vet2 = new Vet(UUID.randomUUID());
        vet2.setId(UUID.randomUUID());
        vet2.setRoomId("Room2");
    }

    @Test
    @DisplayName("Test save vet")
    void testSaveVet() {
        when(vetRepository.save(vet1)).thenReturn(vet1);
        assertThat(vetService.save(vet1)).isEqualTo(vet1);
    }

    @Test
    @DisplayName("Test find vet by id")
    void testFindVetById() {
        when(vetRepository.findById(vet1.getId())).thenReturn(vet1);
        assertThat(vetService.findById(vet1.getId())).isEqualTo(vet1);
    }

    @Test
    @DisplayName("Test delete vet")
    void testDeleteVet() {
        when(vetRepository.existsById(vet1.getId())).thenReturn(true);
        vetService.delete(vet1);
        verify(vetRepository, times(1)).deleteById(vet1.getId());
    }

    @Test
    @DisplayName("Test delete vet by id")
    void testDeleteVetById() {
        when(vetRepository.existsById(vet1.getId())).thenReturn(true);
        vetService.deleteById(vet1.getId());
        verify(vetRepository, times(1)).deleteById(vet1.getId());
    }

    @Test
    @DisplayName("Test delete vet by id not found")
    void testDeleteVetByIdNotFound() {
        when(vetRepository.existsById(vet1.getId())).thenReturn(false);
        vetService.deleteById(vet1.getId());
        verify(vetRepository, never()).deleteById(vet1.getId());
    }

    @Test
    @DisplayName("Test exists vet by id")
    void testExistsVetById() {
        when(vetRepository.existsById(vet1.getId())).thenReturn(true);
        assertThat(vetService.existsById(vet1.getId())).isTrue();
    }

    @Test
    @DisplayName("Test exists vet by id not found")
    void testExistsVetByIdNotFound() {
        when(vetRepository.existsById(vet1.getId())).thenReturn(false);
        assertThat(vetService.existsById(vet1.getId())).isFalse();
    }

    @Test
    @DisplayName("Test exists vet by user id")
    void testExistsVetByUserId() {
        when(vetRepository.existsByUserId(vet1.getUserId())).thenReturn(true);
        assertThat(vetService.existsByUserId(vet1.getUserId())).isTrue();
    }

    @Test
    @DisplayName("Test exists vet by user id not found")
    void testExistsVetByUserIdNotFound() {
        when(vetRepository.existsByUserId(vet1.getUserId())).thenReturn(false);
        assertThat(vetService.existsByUserId(vet1.getUserId())).isFalse();
    }
}