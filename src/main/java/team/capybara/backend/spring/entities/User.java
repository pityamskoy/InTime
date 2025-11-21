package team.capybara.backend.spring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table(name = "Users")
public class User implements EntityWithId {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "shopOwner", nullable = false)
    private Boolean isShopOwner;

    @Override
    public UUID getId() {
        return id;
    }

}
