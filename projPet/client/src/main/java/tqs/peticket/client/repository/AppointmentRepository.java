package tqs.peticket.client.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tqs.peticket.client.model.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
 
    Appointment findById(UUID id);
    void deleteById(UUID id);
    Boolean existsById(UUID id);
    Boolean existsByUserId(UUID userId);
    Boolean existsByPetId(UUID petId);
    Boolean existsByVetId(UUID vetId);
    
    List<Appointment> findByUserId(UUID userId);
    List<Appointment> findByPetId(UUID petId);
    List<Appointment> findByVetId(UUID vetId);

    List<Appointment> findByStatus(String status);
    List<Appointment> findByDateAndTime(String date, String time);
    List<Appointment> findByDate(String date);
    List<Appointment> findByTime(String time);
    Boolean existsByPetIdAndUserId(UUID petId, UUID userId);
}
