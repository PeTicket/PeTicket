package tqs.peticket.vet.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tqs.peticket.vet.model.Vet;

@Repository
public interface VetRepository extends JpaRepository<Vet, Long>{
    Vet findById(UUID id);
    void deleteById(UUID id);
    boolean existsById(UUID id);
    Vet findByUserId(UUID userId);
    Boolean existsByUserId(UUID userId);    
}
