package tqs.peticket.vet.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tqs.peticket.vet.model.Vet;


public interface  VetRepository extends JpaRepository<Vet, Long>{
    Vet findById(UUID id);
    Vet deleteById(UUID id);
    Boolean existsById(UUID id);
    Vet findByUserId(UUID userId);
    Boolean existsByUserId(UUID userId);    
}
