package team.capybara.backend.spring.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Shops")
public class Shop implements EntityWithId {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "openTime", nullable = false)
    private Date timeOpen;

    @Column(name = "closeTime", nullable = false)
    private Date timeClose;

    @Column(name = "registrationDate", nullable = false)
    private Date registrationDate;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "isVerifide", nullable = false)
    private boolean isVerifide;

    @Column(name = "inn", nullable = false)
    private String inn;

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

    @Column(name = "address", nullable = false)
    private String linksToSocialMedia;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @Override
    public UUID getId() {
        return id;
    }

}
