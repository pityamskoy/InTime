package team.capybara.backend.spring.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Shops")
public class Shop {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "isVerifide", nullable = false)
    private boolean isVerifide;

    @Column(name = "mainImagePath", nullable = false)
    private String mainImagePath;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Image> images;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "lat", nullable = false)
    private Double lat; //latitude

    @Column(name = "lon", nullable = false)
    private Double lon; //longitude

}
