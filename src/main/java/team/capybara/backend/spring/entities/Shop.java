package team.capybara.backend.spring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.util.List;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Shops")
public class Shop implements EntityWithId {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "open_time", nullable = false)
    private Time timeOpen;

    @Column(name = "close_time", nullable = false)
    private Time timeClose;

    @Column(name = "registration_date", nullable = false)
    private Date registrationDate;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_Verifide", nullable = false)
    private boolean isVerified;

    @Column(name = "inn", nullable = false)
    private String inn;

    @OneToOne
    @JoinColumn(name = "main_image_id")
    private Image mainImage;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Image> images;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "lat", nullable = false)
    private double lat; //latitude v gradusah

    @Column(name = "lon", nullable = false)
    private double lon; //longitude v gradusah

    @Column(name = "linkToSocialMedia")
    private String linkToSocialMedia;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @Column(name = "phone_number")
    private String organizationPhoneNumber;

    @Column(name = "companyType")
    private String companyType;

    @Transient
    private double distance;

    @Override
    public UUID getId() {
        return id;
    }

    public void setDistance(double objectLat, double objectLon) {
        //distance in kilometers
        double kef = Math.PI / 180.0;
        int earthRadius = 6371;
        this.distance = Math.acos(Math.sin(lat * kef) * Math.sin(objectLat * kef) + Math.cos(lat * kef) * Math.cos(objectLat * kef) * Math.cos((lon * kef) - (objectLon * kef))) * earthRadius;
    }

}
