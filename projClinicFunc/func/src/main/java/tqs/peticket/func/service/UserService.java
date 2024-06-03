package tqs.peticket.func.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqs.peticket.func.model.User;
import tqs.peticket.func.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    public User findByFirstNameAndLastName(String firstName, String lastName) {
        return userRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public void deleteById(UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }

    public User findById(UUID id) {
        return userRepository.findById(id);
    }

    public Boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }

    public User update(UUID userId, User user) {
        if (!userRepository.existsById(userId)) {
            return null;
        }
        user.setId(userId);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
