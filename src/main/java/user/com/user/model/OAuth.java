package user.com.user.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name="oauth")
public class OAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userId;
    private String requestId;
    private Boolean status=false;

    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    private String tokenValue;
    private String accessToken;
    private String refreshToken;

     public OAuth(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

     public OAuth(){
        
     }
    @Column(columnDefinition = "TEXT") // use TEXT for long tokens
    private String oauthData;
}
