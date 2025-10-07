package team.capybara.backend.hibernate;
import com.google.common.hash.Hashing;
import jakarta.persistence.*;

import java.nio.charset.StandardCharsets;
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

    public User(String name, String email, String password){
        this.id = (UUID.randomUUID()).toString();
        this.name = name;
        this.email = email;
        this.password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }

    public String getId() {return id;}

    public String getName() {return name;}

    public String getEmail() {return email;}

    public String getPassword() {return password;}

    public void setName(String name) {this.name = name;}

    public void setEmail(String email) {this.email = email;}

    public void setPassword(String password) {this.password = password;}

}
