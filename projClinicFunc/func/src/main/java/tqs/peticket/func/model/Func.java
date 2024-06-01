package tqs.peticket.func.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import java.util.UUID;

@Entity
@Table(name = "func")
public class Func {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    public Func() {
    }

    public Func(UUID userId) {
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
