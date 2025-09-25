package team.capybara.backend.hibernate;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name="ShopImages")
public class ShopImages {

    @Id
    @Column(name="id", nullable = false, unique = true)
    private String id;

    @OneToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Column(name="imagePaths", nullable = false)
    private ArrayList<String>imagePaths;

    ShopImages(Product product, ArrayList<String> imagePaths){
        this.id = (UUID.randomUUID()).toString();
        this.shop = shop;
        this.imagePaths = imagePaths;
    }

    public String getId() {return id;}

    public Shop getProduct() {return shop;}

    public ArrayList<String> getImagePaths() {return imagePaths;}

}
