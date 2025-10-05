package team.capybara.backend.hibernate;

import jakarta.persistence.*;

import java.util.List;
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
    private boolean isVerifide;

    @Column(name="mainImagePath", nullable = false)
    private String mainImagePath;

    @OneToMany
    private List<Image> images;

    @Column(name="address", nullable = false)
    private String address;

    @Column(name="lat", nullable = false)
    private Double lat; //latitude

    @Column(name="lon", nullable = false)
    private Double lon; //longitude

    public Shop(String name, String description, boolean isVerifide, String mainImagePath, List<Image> images, String address, Double lat, Double lon){
        this.id = (UUID.randomUUID()).toString();
        this.name = name;
        this.description = description;
        this.isVerifide = isVerifide;
        this.mainImagePath = mainImagePath;
        this.images = images;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
    }

    public String getId() {return id;}

    public String getName() {return name;}

    public String getDescription() {return description;}

    public boolean getIsVerifide() {return isVerifide;}

    public String getMainImagePath() {return mainImagePath;}

    public List<Image> getImages() {return images;}

    public String getAddress() {return address;}

    public Double getLat() {return lat;}

    public Double getLon() {return lon;}

    public void setName(String name) {this.name = name;}

    public void setDescription(String description) {this.description = description;}

    public void setIsVerifide(boolean isVerifide) {this.isVerifide = isVerifide;}

    public void setMainImagePath(String mainImagePath) {this.mainImagePath = mainImagePath;}

    public void setImages(List<Image> images) {this.images = images;}

    public void setAddress(String address) {this.address = address;}

    public void setLat(Double lat) {this.lat = lat;}

    public void setLon(Double lon) {this.lon = lon;}

    public void addImagePath(Image image){images.add(image);}
}
