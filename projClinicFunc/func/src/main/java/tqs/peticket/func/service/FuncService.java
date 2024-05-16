package tqs.peticket.func.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqs.peticket.func.model.Func;
import tqs.peticket.func.repository.FuncRepository;


@Service
public class FuncService {

    @Autowired
    private FuncRepository funcRepository;

    public Func save(Func func) {
        return funcRepository.save(func);
    }

    public Func findById(UUID id) {
        return funcRepository.findById(id);
    }

    public void delete(Func func) {
        if (funcRepository.existsById(func.getId())) {
            funcRepository.deleteById(func.getId());
        }
    }

    public void deleteById(UUID id) {
        if (funcRepository.existsById(id)) {
            funcRepository.deleteById(id);
        }
    }

    public Boolean existsById(UUID id) {
        return funcRepository.existsById(id);
    }

    public Boolean existsByUserId(UUID userId) {
        return funcRepository.existsByUserId(userId);
    }    
}
