package team.capybara.backend.hibernate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name="Shops")
public class Shop {

    @Id
    @Column(name="id", nullable = false, unique = true)
    private String id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="isVerifide", nullable = false)
    private int isVerifide;

    @Column(name="mainImagePath", nullable = false)
    private String mainImagePath;

    @Column(name="address", nullable = false)
    private String address;

    @Column(name="lat", nullable = false)
    private Double lat; //latitude

    @Column(name="lon", nullable = false)
    private Double lon; //longitude

    Shop(String name, String description, int isVerifide, String mainImagePath, String address, Double lat, Double lon){
        this.id = (UUID.randomUUID()).toString();
        this.name = name;
        this.description = description;
        this.isVerifide = isVerifide;
        this.mainImagePath = mainImagePath;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
    }

    public Shop() {

    }

    public String getId() {return id;}

    public String getName() {return name;}

    public String getDescription() {return description;}

    public int getIsVerifide() {return isVerifide;}

    public String getMainImagePath() {return mainImagePath;}

    public String getAddress() {return address;}

    public Double getLat() {return lat;}

    public Double getLon() {return lon;}

    public void setName(String name) {this.name = name;}

    public void setDescription(String description) {this.description = description;}

    public void setIsVerifide(int isVerifide) {this.isVerifide = isVerifide;}

    public void setMainImagePath(String mainImagePath) {this.mainImagePath = mainImagePath;}

    public void setAddress(String address) {this.address = address;}

    public void setLat(Double lat) {this.lat = lat;}

    public void setLon(Double lon) {this.lon = lon;}
}
