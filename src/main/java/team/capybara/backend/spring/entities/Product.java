package team.capybara.backend.spring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Products")
public class Product implements EntityWithId {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_type_id")
    private ProductType productType;

    @Column(name = "shelf_life", nullable = false)
    private Date shelfLife; //last day of life

    @Column(name = "price", nullable = false)
    private int price; // initial price of product

    @Column(name = "discount", nullable = false)
    private int discount; // discount amount in monetary units

    @Column(name = "is_sold", nullable = false)
    private boolean isSold; //0 - false; 1 - true

    @Transient
    private double score;

    /**
     * This method is used to compare products and find the most relevant for customers.
     * @param userLat is user's latitude.
     * @param userLon is user's longitude.
     * @return approximate score of a product.
     */
    public Double calculateScore(Double userLat, Double userLon) {
        Shop shop = getProductType().getShop();
        shop.setDistance(userLat, userLon);
        //int rating =
        score = 100*((price-discount*1.0)/(price*1.0)) + shelfLife.getTime() + (50/shop.getDistance());
        return score;
    }

    @Override
    public UUID getId() {
        return id;
    }

}
