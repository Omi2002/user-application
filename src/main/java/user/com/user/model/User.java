package user.com.user.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String mobile;
    private int age;

  
    private String password;

    public User(int id, String name, int age, String email, String mobile, String Address) {
    }

    public User() {

    }
}
