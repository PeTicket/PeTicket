package tqs.peticket.func.service;  
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqs.peticket.func.model.Appointment;
import tqs.peticket.func.model.Pet;
import tqs.peticket.func.repository.AppointmentRepository;


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

    public void delete(Appointment appointment) {
        if (appointmentRepository.existsById(appointment.getId())) {
            appointmentRepository.deleteById(appointment.getId());
        }
    }

    public void deleteById(UUID id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
        }
    }

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

    public Appointment update(UUID appointmentId, Appointment appointment) {
        if (!appointmentRepository.existsById(appointment.getId())) {
            return null;
        }
        appointment.setId(appointmentId);
        return appointmentRepository.save(appointment);
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

    public Appointment findNextAppointment(){
        LocalDate today = LocalDate.now();
        List<Appointment> appointments = appointmentRepository.findByDate(today.toString());
        Appointment nextAppointment = null;
        appointments.sort((a1, a2) -> a1.getAppointment_number().compareTo(a2.getAppointment_number()));
        for (Appointment appointment : appointments) {
            if (appointment.getStatus().equals("on_hold")) {
                nextAppointment = appointment;
                return nextAppointment;
            }
        }
        return null;

    }

    public int getLastAppointmentNumber() {
        LocalDate today = LocalDate.now();
        List<Appointment> appointments = appointmentRepository.findByDate(today.toString());
        int lastAppointmentNumber = 0;
        for (Appointment appointment : appointments) {
            if (appointment.getAppointment_number() > lastAppointmentNumber) {
                lastAppointmentNumber = appointment.getAppointment_number();
            }
        }
        return lastAppointmentNumber;
    }
}
