package team.capybara.backend.spring.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Products")
public class Product {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_type_id")
    private ProductType productType;

    @Column(name = "shelf_life", nullable = false)
    private Date shelfLife; //last day of life

    @Column(name = "price", nullable = false)
    private int price; // final price of product

    @Column(name = "discount", nullable = false)
    private int discount; // discount amount in monetary units

    @Column(name = "is_sold", nullable = false)
    private boolean isSold; //0 - false; 1 - true

}
