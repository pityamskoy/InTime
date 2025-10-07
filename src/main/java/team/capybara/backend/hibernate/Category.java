package team.capybara.backend.hibernate;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
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

    @OneToMany
    private List<ProductType> productTypes;

    public Category(String name, String description, ArrayList<ProductType> productTypes){
        this.id = (UUID.randomUUID()).toString();
        this.name = name;
        this.description = description;
        this.productTypes = productTypes;
    }

    public String getId(){return id;}

    public String getName(){return name;}

    public String getDescription(){return description;}

    public List<ProductType> getProductTypes() {return productTypes;}

    public void setName(String name) {this.name = name;}

    public void setDescription(String description) {this.description = description;}

    public void setProductTypes(List<ProductType> productTypes) {this.productTypes = productTypes;}
}
