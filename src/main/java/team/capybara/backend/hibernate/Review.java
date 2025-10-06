package team.capybara.backend.hibernate;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="Reviews")
public class Review {

    @Id
    @Column(name="id", nullable = false, unique = true)
    private String id;

    @ManyToOne
    private User user;

    @OneToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @OneToOne
    @JoinColumn(name = "product_type_id")
    private ProductType productType;

    @Column(name="text", nullable = false)
    private String text;

    @Column(name="stars", nullable = false)
    private int stars; // 1-5

    Review(User user, Shop shop, ProductType productType, String text, int stars){
        this.id = (UUID.randomUUID()).toString();
        this.user = user;
        this.shop = shop;
        this.productType = productType;
        this.text = text;
        this.stars = stars;
    }

    Review(User user, Shop shop, String text, int stars){
        this.id = (UUID.randomUUID()).toString();
        this.user = user;
        this.shop = shop;
        this.productType = null;
        this.text = text;
        this.stars = stars;
    }

    public Review() {

    }

    public String getId() {return id;}

    public User getUser() {return user;}

    public Shop getShop() {return shop;}

    public ProductType getProductType() {return productType;}

    public String getText() {return text;}

    public int getStars() {return stars;}

    public void setUser(User user) {this.user = user;}

    public void setShop(Shop shop) {this.shop = shop;}

    public void setProductType(ProductType productType) {this.productType = productType;}

    public void setText(String text) {this.text = text;}

    public void setStars(int stars) {this.stars = stars;}
}
