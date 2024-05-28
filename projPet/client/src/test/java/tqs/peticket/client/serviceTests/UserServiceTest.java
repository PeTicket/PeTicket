package tqs.peticket.client.serviceTests;

import tqs.peticket.client.service.UserService;
import tqs.peticket.client.model.User;
import tqs.peticket.client.repository.UserRepository;

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
    @DisplayName("Test deleteById not found")
    void testDeleteByIdNotFound() {
        when(userRepository.existsById(user1.getId())).thenReturn(false);
        userService.deleteById(user1.getId());
        verify(userRepository, never()).deleteById(user1.getId());
    }

    @Test
    @DisplayName("Test update")
    void testUpdate() {
        when(userRepository.existsById(user1.getId())).thenReturn(true);
        when(userRepository.save(user1)).thenReturn(user1);
        User updatedUser = userService.update(user1);
        assertThat(updatedUser).isEqualTo(user1);
    }

    @Test
    @DisplayName("Test update non-existing user")
    void testUpdateNonExistingUser() {
        when(userRepository.existsById(user1.getId())).thenReturn(false);
        User updatedUser = userService.update(user1);
        assertNull(updatedUser);
    }

    @Test
    @DisplayName("Test updateById")
    void testUpdateById() {
        when(userRepository.existsById(user1.getId())).thenReturn(true);
        when(userRepository.save(user1)).thenReturn(user1);
        User updatedUser = userService.updateById(user1.getId(), user1);
        assertThat(updatedUser).isEqualTo(user1);
    }

    @Test
    @DisplayName("Test updateById non-existing user")
    void testUpdateByIdNonExistingUser() {
        when(userRepository.existsById(user1.getId())).thenReturn(false);
        User updatedUser = userService.updateById(user1.getId(), user1);
        assertNull(updatedUser);
    }

    @Test
    @DisplayName("Test findById")
    void testFindById() {
        when(userRepository.findById(user1.getId())).thenReturn(user1);
        User found = userService.findById(user1.getId());
        assertThat(found).isEqualTo(user1);
    }

    @Test
    @DisplayName("Test findById not found")
    void testFindByIdNotFound() {
        when(userRepository.findById(user1.getId())).thenReturn(null);
        User found = userService.findById(user1.getId());
        assertNull(found);
    }

    @Test
    @DisplayName("Test existsById")
    void testExistsById() {
        when(userRepository.existsById(user1.getId())).thenReturn(true);
        Boolean exists = userService.existsById(user1.getId());
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Test getAllUsers")
    void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        when(userRepository.findAll()).thenReturn(users);
        List<User> allUsers = userService.getAllUsers();
        assertThat(allUsers).isEqualTo(users);
    }

    @Test
    @DisplayName("Test getAllUsers empty")
    void testGetAllUsersEmpty() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        List<User> allUsers = userService.getAllUsers();
        assertThat(allUsers).isEmpty();
    }
    
}
