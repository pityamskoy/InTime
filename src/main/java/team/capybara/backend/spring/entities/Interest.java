package team.capybara.backend.spring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table(name = "Interests")
public class Interest implements EntityWithId {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "time", nullable = false)
    private Date time;

    @Override
    public UUID getId() {
        return id;
    }

}
