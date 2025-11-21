package team.capybara.backend.spring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

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

    public void calculateScore(double distance,int raiting){
        this.score = 100*((price-discount*1.0)/(price*1.0)) + shelfLife.getTime() + (50/distance) + raiting*10;
    }

    @Override
    public UUID getId() {
        return id;
    }

}
