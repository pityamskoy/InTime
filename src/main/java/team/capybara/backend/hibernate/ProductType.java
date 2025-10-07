package team.capybara.backend.hibernate;
import jakarta.persistence.*;

import java.util.List;
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

    @OneToMany
    private List<Image> images;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    public ProductType(String name, String description, String mainImagePath, List<Image> images,Shop shop){
        this.id = (UUID.randomUUID()).toString();
        this.name = name;
        this.description = description;
        this.mainImagePath = mainImagePath;
        this.images = images;
        this.shop = shop;
    }

    public String getId(){return id;}

    public String getName(){return name;}

    public String getDescription(){return description;}

    public String getMainImagePath(){return mainImagePath;}

    public List<Image> getImages() {return images;}

    public Shop getShop(){return shop;}

    public void setName(String name) {this.name = name;}

    public void setDescription(String description) {this.description = description;}

    public void setMainImagePath(String mainImagePath) {this.mainImagePath = mainImagePath;}

    public void setImages(List<Image> images) {this.images = images;}

    public void setShop(Shop shop) {this.shop = shop;}

    public void addImagePath(Image image){images.add(image);}
}
