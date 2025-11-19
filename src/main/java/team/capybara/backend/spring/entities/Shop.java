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

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "isVerifide", nullable = false)
    private boolean isVerified;

    @Column(name = "inn", nullable = false)
    private String inn;

    @Column(name = "mainImagePath", nullable = false)
    private String mainImagePath;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Image> images;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "lat", nullable = false)
    private double lat; //latitude v gradusah

    @Column(name = "lon", nullable = false)
    private double lon; //longitude v gradusah

    @Column(name = "linksToSocialMedia")
    private String linkToSocialMedia;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @Column(name = "companyType")
    private String companyType;

    @Transient
    private double ditance;

    @Override
    public UUID getId() {
        return id;
    }

    public void getDistanceTo(double objectLat, double objectLon) {
        //distance in kilometers
        double kef = 3.14 / 180.0;
        int earthRadius = 6371;
        ditance = Math.acos(Math.sin(lat * kef) * Math.sin(objectLat * kef) + Math.cos(lat * kef) * Math.cos(objectLat * kef) * Math.cos((lon * kef) - (objectLon * kef))) * earthRadius;
    }

}
