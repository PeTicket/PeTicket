package tqs.peticket.func.serviceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.peticket.func.model.Func;
import tqs.peticket.func.repository.FuncRepository;
import tqs.peticket.func.service.FuncService;

@ExtendWith(MockitoExtension.class)
public class FuncServiceTests {

    @Mock
    private FuncRepository funcRepository;

    @InjectMocks
    private FuncService funcService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        Func func = new Func();
        when(funcRepository.save(func)).thenReturn(func);

        Func savedFunc = funcService.save(func);
        assertNotNull(savedFunc);
        verify(funcRepository, times(1)).save(func);
    }

    @Test
    public void testFindById() {
        UUID id = UUID.randomUUID();
        Func func = new Func();
        when(funcRepository.findById(id)).thenReturn(func);

        Func foundFunc = funcService.findById(id);
        assertNotNull(foundFunc);
        verify(funcRepository, times(1)).findById(id);
    }

    @Test
    public void testDelete() {
        Func func = new Func();
        UUID id = UUID.randomUUID();
        func.setId(id);
        when(funcRepository.existsById(id)).thenReturn(true);

        funcService.delete(func);
        verify(funcRepository, times(1)).existsById(id);
        verify(funcRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteById() {
        UUID id = UUID.randomUUID();
        when(funcRepository.existsById(id)).thenReturn(true);

        funcService.deleteById(id);
        verify(funcRepository, times(1)).existsById(id);
        verify(funcRepository, times(1)).deleteById(id);
    }

    @Test
    public void testExistsById() {
        UUID id = UUID.randomUUID();
        when(funcRepository.existsById(id)).thenReturn(true);

        Boolean exists = funcService.existsById(id);
        assertTrue(exists);
        verify(funcRepository, times(1)).existsById(id);
    }

    @Test
    public void testExistsByUserId() {
        UUID userId = UUID.randomUUID();
        when(funcRepository.existsByUserId(userId)).thenReturn(true);

        Boolean exists = funcService.existsByUserId(userId);
        assertTrue(exists);
        verify(funcRepository, times(1)).existsByUserId(userId);
    }
}

