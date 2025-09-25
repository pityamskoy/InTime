package team.capybara.backend.hibernate;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name="ProductTypes")
public class ProductType {

    @Id
    @Column(name="id", nullable = false, unique = true)
    private String id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="main_image_path", nullable = false)
    private String mainImagePath;

    @OneToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne
    private Category category;

    ProductType(String name, String description, String mainImagePath, Shop shop, Category category){
        this.id = (UUID.randomUUID()).toString();
        this.name = name;
        this.description = description;
        this.mainImagePath = mainImagePath;
        this.shop = shop;
        this.category = category;
    }

    public String getId(){return id;}

    public String getName(){return name;}

    public String getDescription(){return description;}

    public String getMainImagePath(){return mainImagePath;}

    public Shop getShop(){return shop;}

    public Category getCategory(){return category;}

    public void setName(String name) {this.name = name;}

    public void setDescription(String description) {this.description = description;}

    public void setMainImagePath(String mainImagePath) {this.mainImagePath = mainImagePath;}

    public void setShop(Shop shop) {this.shop = shop;}

    public void setCategory(Category category) {this.category = category;}
}
