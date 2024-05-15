package tqs.peticket.client.serviceTests;

import tqs.peticket.client.service.UserService;
import tqs.peticket.client.model.User;
import tqs.peticket.client.repository.UserRepository;

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
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setEmail("user1@example.com");
        user1.setFirstName("Afonso");
        user1.setLastName("Silva");
        user1.setPhone("912345678");
        user1.setAddress("Rua do Afonso");
        user1.setPassword("password");

        user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setEmail("user2@example.com");
        user2.setFirstName("Francisco");
        user2.setLastName("Ponte");
        user2.setPhone("912345679");
        user2.setAddress("Rua do Francisco");
        user2.setPassword("password");
    }

    @Test
    @DisplayName("Test findByEmail")
    void testFindByEmail() {
        when(userRepository.findByEmail("user1@example.com")).thenReturn(user1);
        User found = userService.findByEmail(user1.getEmail());
        assertThat(found).isEqualTo(user1);
    }

    @Test
    @DisplayName("Test existsByEmail")
    void testExistsByEmail() {
        when(userRepository.existsByEmail("user1@example.com")).thenReturn(true);
        Boolean found = userService.existsByEmail(user1.getEmail());
        assertThat(found).isEqualTo(true);
    }

    @Test
    @DisplayName("Test findByFirstNameAndLastName")
    void testFindByFirstNameAndLastName() {
        when(userRepository.findByFirstNameAndLastName("Afonso", "Silva")).thenReturn(user1);
        User found = userService.findByFirstNameAndLastName(user1.getFirstName(), user1.getLastName());
        assertThat(found).isEqualTo(user1);
    }

    @Test
    @DisplayName("Test findByPhone")
    void testFindByPhone() {
        when(userRepository.findByPhone("912345679")).thenReturn(user2);
        User found = userService.findByPhone(user2.getPhone());
        assertThat(found).isEqualTo(user2);
    }

    @Test
    @DisplayName("Test save")
    void testSave() {
        when(userRepository.save(user1)).thenReturn(user1);
        User saved = userService.save(user1);
        assertThat(saved).isEqualTo(user1);
    }

    @Test
    @DisplayName("Test delete")
    void testDelete() {
        userService.delete(user1);
        verify(userRepository, times(1)).delete(user1);
    }

    @Test
    @DisplayName("Test deleteById")
    void testDeleteById() {
        when(userRepository.existsById(user1.getId())).thenReturn(true);
        userService.deleteById(user1.getId());
        verify(userRepository, times(1)).deleteById(user1.getId());
    }

    @Test
    @DisplayName("Test update")
    void testUpdate() {
        User updatedUser = new User();
        updatedUser.setId(UUID.randomUUID());
        updatedUser.setEmail("updateduser@example.com");
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");
        updatedUser.setPhone("912345444");
        updatedUser.setAddress("Rua do Updated");
        updatedUser.setPassword("password");

        when(userRepository.existsById(user1.getId())).thenReturn(true);
        when(userRepository.save(user1)).thenReturn(updatedUser);
        assertThat(userService.update(user1)).isEqualTo(updatedUser);

    }



    
}
