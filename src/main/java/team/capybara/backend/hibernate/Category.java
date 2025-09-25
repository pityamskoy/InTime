package team.capybara.backend.hibernate;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name="Category")
public class Category {

    @Id
    @Column(name="id", nullable = false, unique = true)
    private String id;

    @Column(name="name", nullable = false, unique = true)
    private String name;

    @Column(name="description", nullable = false, unique = true)
    private String description;

    @OneToMany(mappedBy = "catrgory")
    private ArrayList<ProductType> productTypes;

    Category(String name, String description, ArrayList<ProductType> productTypes){
        this.id = (UUID.randomUUID()).toString();
        this.name = name;
        this.description = description;
        this.productTypes = productTypes;
    }

    public String getId(){return id;}

    public String getName(){return name;}

    public String getDescription(){return description;}

    public ArrayList<ProductType> getProductTypes() {return productTypes;}
}
