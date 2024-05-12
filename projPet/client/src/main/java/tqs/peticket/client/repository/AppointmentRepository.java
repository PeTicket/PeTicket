package tqs.peticket.client.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import tqs.peticket.client.model.Appointment;


public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
 
    Appointment findById(UUID id);
    Appointment deleteById(UUID id);
    Boolean existsById(UUID id);
    Boolean existsByUserId(UUID userId);
    Boolean existsByPetId(UUID petId);
    Boolean existsByVetId(UUID vetId);
    
    Appointment findByUserId(UUID userId);
    Appointment findByPetId(UUID petId);
    Appointment findByVetId(UUID vetId);

    List<Appointment> findByStatus(String status);
    List<Appointment> findByDateAndTime(LocalDate date, LocalTime time);
    List<Appointment> findByDate(LocalDate date);
    List<Appointment> findByTime(LocalTime time);
}
