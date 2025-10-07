package team.capybara.backend.hibernate;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="Images")
public class Image {

    @Id
    @Column(name="id", nullable = false, unique = true)
    private String id;

    @Column(name="image_paths", nullable = false)
    private String path;

    public Image(String path){
        this.id = (UUID.randomUUID()).toString();
        this.path = path;
    }

    public String getId() {return id;}

    public String getPath() {return path;}

    public void setPath(String path) {this.path = path;}

}
