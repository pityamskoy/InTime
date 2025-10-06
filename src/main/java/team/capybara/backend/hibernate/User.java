package team.capybara.backend.hibernate;
import com.google.common.hash.Hashing;
import jakarta.persistence.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name="Users")
public class User {

    @Id
    @Column(name="id", nullable = false, unique = true)
    private String id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    /*@OneToMany(mappedBy = "user")
    private ArrayList<Review> reviews;

    @OneToMany(mappedBy = "user")
    private ArrayList<Interests> interests;

    @OneToMany(mappedBy = "user")
    private ArrayList<Favorite> favorite;*/

    User(String name, String email, String password,ArrayList<Review> reviews, ArrayList<Interests> interests, ArrayList<Favorite> favorite){
        this.id = (UUID.randomUUID()).toString();
        this.name = name;
        this.email = email;
        this.password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        /*this.reviews = reviews;
        this.interests = interests;
        this.favorite = favorite;*/
    }

    public User() {

    }

    public String getId() {return id;}

    public String getName() {return name;}

    public String getEmail() {return email;}

    public String getPassword() {return password;}

    /*public ArrayList<Review> getReviews() {return reviews;}

    public ArrayList<Interests> getInterests() {return interests;}

    public ArrayList<Favorite> getFavorite(){return favorite;}*/

    public void setName(String name) {this.name = name;}

    public void setEmail(String email) {this.email = email;}

    public void setPassword(String password) {this.password = password;}

    /*public void setReviews(ArrayList<Review> reviews) {this.reviews = reviews;}

    public void setInterests(ArrayList<Interests> interests) {this.interests = interests;}

    public void setFavorite(ArrayList<Favorite> favorite) {this.favorite = favorite;}*/
}
