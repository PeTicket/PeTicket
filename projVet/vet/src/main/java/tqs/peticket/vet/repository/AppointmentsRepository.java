package tqs.peticket.vet.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import tqs.peticket.vet.model.Appointment;

public interface AppointmentsRepository extends JpaRepository<Appointment, Long> {

    Appointment findById(UUID id);
    Boolean existsById(UUID id);
    List<Appointment> findByUserId(UUID userId);
    List<Appointment> findByPetId(UUID petId);
    List<Appointment> findByVetId(UUID vetId);
    List<Appointment> findByDateAndTime(String date, String time);
    List<Appointment> findByDate(String date);
    List<Appointment> findByTime(String time);
    List<Appointment> findAll();
}
