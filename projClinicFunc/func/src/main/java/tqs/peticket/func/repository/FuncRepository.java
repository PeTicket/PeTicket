package tqs.peticket.func.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tqs.peticket.func.model.Func;

@Repository
public interface  FuncRepository extends JpaRepository<Func, Long>{
    Func findById(UUID id);
    void deleteById(UUID id);
    Boolean existsById(UUID id);
    Func findByUserId(UUID userId);
    Boolean existsByUserId(UUID userId);
    Func save(Func func);    
}
