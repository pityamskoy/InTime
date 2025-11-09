package team.capybara.backend.spring.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Product_types")
public class ProductType implements EntityWithId {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "weight", nullable = false)
    private Double weight;// вес в граммах

    @Column(name = "calories")
    private Double calories;

    @Column(name = "squirrels")
    private Double squirrels;

    @Column(name = "fats")
    private Double fats;

    @Column(name = "carbohydrates")
    private Double carbohydrates;

    @Column(name = "quantityInOnePackage")
    private int quantityInOnePackage;

    @Column(name = "mainImagePath", nullable = false)
    private String mainImagePath;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Image> images;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Override
    public UUID getId() {
        return id;
    }

}
