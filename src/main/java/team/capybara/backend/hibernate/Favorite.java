package team.capybara.backend.hibernate;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="Favorite")
public class Favorite {

    @Id
    @Column(name="id", nullable = false, unique = true)
    private String id;

    /*@ManyToOne
    @Column(name="user_id", nullable = false)
    private User user;*/

    @OneToOne
    @JoinColumn(name = "product_type_id")
    private ProductType productType;

    Favorite(User user, ProductType productType) {
        this.id = (UUID.randomUUID()).toString();
//        this.user = user;
        this.productType = productType;
    }

    public Favorite() {

    }

    public String getId() {return id;}

//    public User getUser() {return user;}

    public ProductType getProduct() {return productType;}

//    public void setUser(User user) {this.user = user;}

    public void setProductType(ProductType productType) {this.productType = productType;}
}