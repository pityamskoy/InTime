package team.capybara.backend.hibernate;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="Interests")
public class Interests {

    @Id
    @Column(name="id", nullable = false, unique = true)
    private String id;

    /*@ManyToOne
    @Column(name="user_id", nullable = false)
    private User user;*/

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name="time", nullable = false)
    private Date time;

    Interests(User user, Product product, Date time){
        this.id = (UUID.randomUUID()).toString();
//        this.user = user;
        this.product = product;
        this.time = time;
    }

    public Interests() {

    }

    public String getId() {return id;}

//    public User getUser() {return user;}

    public Product getProduct() {return product;}

    public Date getTime() {return time;}

//    public void setUser(User user) {this.user = user;}

    public void setProduct(Product product) {this.product = product;}

    public void setTime(Date time) {this.time = time;}
}
