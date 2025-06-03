package user.com.user.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class SessionUser implements Serializable{
    private Integer id;
    private String email;
    public SessionUser(Integer id, String email) {
        this.id = id;
        this.email = email;
    }
}
