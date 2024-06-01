package tqs.peticket.func.repository;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tqs.peticket.func.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(UUID Id);
    void deleteById(UUID Id);
    Boolean existsById(UUID Id);
    User findByEmail(String email);
    Boolean existsByEmail(String email);
    User findByFirstNameAndLastName(String firstName, String lastName);
    User findByPhone(String phone);
}