package tqs.peticket.vet.serviceTests;

import tqs.peticket.vet.service.UserService;
import tqs.peticket.vet.model.User;
import tqs.peticket.vet.repository.UserRepository;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

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
    @DisplayName("Test findById")
    void testFindById() {
        when(userRepository.findById(user1.getId())).thenReturn(user1);
        User found = userService.findById(user1.getId());
        assertThat(found).isEqualTo(user1);
    }

    @Test
    @DisplayName("Test existsById")
    void testexistsById() {
        when(userRepository.existsById(user1.getId())).thenReturn(true);
        boolean found = userService.existsById(user1.getId());
        assertThat(found).isTrue();
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
    @Test
    @DisplayName("Test getAllUsers")
    void testgetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        List<User> lst = userService.getAllUsers();
        assertThat(lst).hasSize(2).contains(user1, user2);
    }
    
}

