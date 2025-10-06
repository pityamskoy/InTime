package team.capybara.backend.hibernate;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
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

    /*@OneToMany
    private List<String> imagePaths;*/

    ProductImages(ProductType productType, ArrayList<String> imagePaths){
        this.id = (UUID.randomUUID()).toString();
        this.productType = productType;
//        this.imagePaths = imagePaths;
    }

    public ProductImages() {

    }

    public String getId() {return id;}

    public ProductType getProductType() {return productType;}

//    public List<String> getImagePaths() {return imagePaths;}

    public void setProductType(ProductType productType) {this.productType = productType;}

//    public void setImagePaths(ArrayList<String> imagePaths) {this.imagePaths = imagePaths;}
}
