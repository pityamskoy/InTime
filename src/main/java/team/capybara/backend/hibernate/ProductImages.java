package team.capybara.backend.hibernate;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name="ProductImages")
public class ProductImages {

    @Id
    @Column(name="id", nullable = false, unique = true)
    private String id;

    @OneToOne
    @JoinColumn(name = "product_type_id")
    private ProductType productType;

    @Column(name="image_paths", nullable = false)
    private ArrayList<String> imagePaths;

    ProductImages(ProductType productType, ArrayList<String> imagePaths){
        this.id = (UUID.randomUUID()).toString();
        this.productType = productType;
        this.imagePaths = imagePaths;
    }

    public String getId() {return id;}

    public ProductType getProduct() {return productType;}

    public ArrayList<String> getImagePaths() {return imagePaths;}

}
