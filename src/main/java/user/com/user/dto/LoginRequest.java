package user.com.user.dto;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
