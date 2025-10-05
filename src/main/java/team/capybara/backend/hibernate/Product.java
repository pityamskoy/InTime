package team.capybara.backend.hibernate;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="Products")
public class Product {

    @Id
    @Column(name="id", nullable = false, unique = true)
    private String id;

    @OneToOne
    @JoinColumn(name = "product_type_id")
    private ProductType productType;

    @Column(name="shelf_life", nullable = false)
    private Date shelfLife; //last day of life

    @Column(name="price", nullable = false)
    private int price; // final price of product

    @Column(name="discount", nullable = false)
    private int discount; // discount amount in monetary units

    @Column(name="is_sold", nullable = false)
    private int isSold; //0 - false; 1 - true

    public Product() {

    }

    public Product(ProductType productType,Date shelfLife,int price,int discount,int isSold){
        this.id = (UUID.randomUUID()).toString();
        this.productType = productType;
        this.shelfLife = shelfLife;
        this.price = price;
        this.discount = discount;
        this.isSold = isSold;
    }

    public String getId(){return id;}

    public ProductType getProductType(){return productType;}

    public Date getShelfLife(){return shelfLife;}

    public int getPrice(){return price;}

    public int getDiscount(){return discount;}

    public int getIsSold(){return isSold;}

    public void setProductType(ProductType productType) {this.productType = productType;}

    public void setShelfLife(Date shelfLife) {this.shelfLife = shelfLife;}

    public void setPrice(int price) {this.price = price;}

    public void setDiscount(int discount) {this.discount = discount;}

    public void setIsSold(int isSold) {this.isSold = isSold;}
}
