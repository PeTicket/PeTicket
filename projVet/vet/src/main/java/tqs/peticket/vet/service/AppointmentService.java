package tqs.peticket.vet.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqs.peticket.vet.model.Appointment;
import tqs.peticket.vet.model.Pet;
import tqs.peticket.vet.model.User;
import tqs.peticket.vet.model.Vet;
import tqs.peticket.vet.repository.AppointmentsRepository;
import tqs.peticket.vet.repository.PetRepository;
import tqs.peticket.vet.repository.UserRepository;
import tqs.peticket.vet.repository.VetRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentsRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PetRepository petRepository;
    
    @Autowired
    private VetRepository vetRepository;

    public void changeStatus(Appointment appointment, String status){ //Done; In Progress; Done; On Hold
        if (appointmentRepository.existsById(appointment.getId())) {
            appointmentRepository.findById(appointment.getId()).setStatus(status);
        }
    }

    public void addDiagnose(Appointment appointment, String diagnose){
        if (appointmentRepository.existsById(appointment.getId())) {
            appointmentRepository.findById(appointment.getId()).setDiagnosis(diagnose);
        }
    }

    public void addPrescription(Appointment appointment, String presc){
        if (appointmentRepository.existsById(appointment.getId())) {
            appointmentRepository.findById(appointment.getId()).setDiagnosis(presc);
        }
    }

    public void addObservations(Appointment appointment, String obs){
        if (appointmentRepository.existsById(appointment.getId())) {
            appointmentRepository.findById(appointment.getId()).setDiagnosis(obs);
        }
    }

    public Appointment findById(UUID id) {
        return appointmentRepository.findById(id);
    }
    
    public Boolean existsById(UUID id) {
        return appointmentRepository.existsById(id);
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

     public List<Appointment> findByDateAndTime(String date, String time) {
        return appointmentRepository.findByDateAndTime(date, time);
    }

    public List<Appointment> findByDate(String date) {
        return appointmentRepository.findByDate(date);
    }

    public List<Appointment> findByTime(String time) {
        return appointmentRepository.findByTime(time);
    }

    public List<Appointment> findAll() {
            List<Appointment> appointments = appointmentRepository.findAll();
            for (Appointment appointment : appointments) {
                UUID userId = appointment.getUserId();
                UUID petId = appointment.getPetId();
                UUID vetId = appointment.getVetId();

                User user = userRepository.findById(userId);
                Pet pet = petRepository.findById(petId);

                appointment.setUser(user);
                appointment.setPet(pet);

            }
            return appointments;
        }
}
