package tqs.peticket.client.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tqs.peticket.client.model.Appointment;
import tqs.peticket.client.repository.AppointmentRepository;


@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public Appointment findById(UUID id) {
        return appointmentRepository.findById(id);
    }

    @Transactional
    public void delete(Appointment appointment) {
        if (appointmentRepository.existsById(appointment.getId())) {
            appointmentRepository.deleteById(appointment.getId());
        }
    }

    @Transactional
    public void deleteById(UUID id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
        }
    }

    @Transactional
    public Boolean existsById(UUID id) {
        return appointmentRepository.existsById(id);
    }

    public Boolean existsByUserId(UUID userId) {
        return appointmentRepository.existsByUserId(userId);
    }

    public Boolean existsByPetId(UUID petId) {
        return appointmentRepository.existsByPetId(petId);
    }

    public Boolean existsByVetId(UUID vetId) {
        return appointmentRepository.existsByVetId(vetId);
    }

    public Appointment update(Appointment appointment) {
        if (appointmentRepository.existsById(appointment
                .getId())) {
            return appointmentRepository.save(appointment);
        } else {
            return null;
        }
    }

    public List<Appointment> findByUserId(UUID userId) {
        return appointmentRepository.findByUserId(userId);
    }

    public List<Appointment> findByPetId(UUID petId) {
        return appointmentRepository.findByPetId(petId);
    }

    public List<Appointment> findByVetId(UUID vetId) {
        return appointmentRepository.findByVetId(vetId);
    }

    public List<Appointment> findByStatus(String status) {
        return appointmentRepository.findByStatus(status);
    }

    public List<Appointment> findByDateAndTime(String date, String time) {
        return appointmentRepository.findByDateAndTime(date, time);
    }

    public List<Appointment> findByDate(String date) {
        return appointmentRepository.findByDate(date);
    }

    public List<Appointment> findByTime(String time) {
        return appointmentRepository.findByTime(time);
    }
    
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public boolean existsPetByUserId(UUID userId, UUID petId) {
        return appointmentRepository.existsByPetIdAndUserId(petId, userId);
    }
}
