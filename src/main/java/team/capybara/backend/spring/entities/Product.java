package team.capybara.backend.spring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

import static team.capybara.backend.spring.Constants.EPSILON;

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
     * {@code calculateScore} is used to calculate score for products.
     * This method should be used only in {@link EntityHandler}.
     * @param userLat is user's latitude.
     * @param userLon is user's longitude.
     */
    void calculateScore(Double userLat, Double userLon, Double rating) {
        Shop shop = getProductType().getShop();
        shop.calculateDistance(userLat, userLon);

        if (shop.getDistance() < EPSILON) {
            this.score = 1000;
            return;
        }

        if (price < 30) {
            this.score = 20 + ((shelfLife.getTime()) + (100 / shop.getDistance()) + (rating * 10));
        } else {
            this.score = (20 * ((price * 1.0 * discount / 100) / price)) + (shelfLife.getTime()) + (100 / shop.getDistance()) + (rating * 10);
        }
    }

    @Override
    public UUID getId() {
        return id;
    }

}
