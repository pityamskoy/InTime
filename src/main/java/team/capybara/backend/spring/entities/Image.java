package team.capybara.backend.spring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Images")
public class Image implements EntityWithId {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "image_paths", nullable = false)
    private String path;

    @Override
    public UUID getId() {
        return id;
    }

}
