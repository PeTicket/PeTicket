package tqs.peticket.client.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import java.util.UUID;

@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    private String name;
    private String type;
    private String breed;
    private String color;
    private String age;
    private String weight;
    private String height;
    @Column(name = "blood_type")
    private String bloodType;
    @Column(name = "medical_info")
    private String medicalInfo;

    public Pet() {
    }

    public Pet(UUID userId, String name, String type, String breed, String color, String age) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.color = color;
        this.age = age;
    }

    public Pet(UUID userId, String name, String type, String breed, String color, String age, String weight, String height, String bloodType, String medicalInfo) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.color = color;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.bloodType = bloodType;
        this.medicalInfo = medicalInfo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getMedicalInfo() {
        return medicalInfo;
    }

    public void setMedicalInfo(String medicalInfo) {
        this.medicalInfo = medicalInfo;
    }    
}
