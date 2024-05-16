package tqs.peticket.vet.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqs.peticket.vet.model.Vet;
import tqs.peticket.vet.repository.VetRepository;


@Service
public class VetService {

    @Autowired
    private VetRepository vetRepository;

    public Vet save(Vet func) {
        return vetRepository.save(func);
    }

    public Vet findById(UUID id) {
        return vetRepository.findById(id);
    }

    public void delete(Vet func) {
        if (vetRepository.existsById(func.getId())) {
            vetRepository.deleteById(func.getId());
        }
    }

    public void deleteById(UUID id) {
        if (vetRepository.existsById(id)) {
            vetRepository.deleteById(id);
        }
    }

    public Boolean existsById(UUID id) {
        return vetRepository.existsById(id);
    }

    public Boolean existsByUserId(UUID userId) {
        return vetRepository.existsByUserId(userId);
    }    
}
