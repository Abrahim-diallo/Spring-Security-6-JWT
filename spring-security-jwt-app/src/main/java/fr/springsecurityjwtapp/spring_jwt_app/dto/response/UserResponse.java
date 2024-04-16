package fr.springsecurityjwtapp.spring_jwt_app.dto.response;

import fr.springsecurityjwtapp.spring_jwt_app.entity.RefreshToken;
import lombok.Data;

import java.util.Set;
@Data
public class UserResponse {
    private Long id;
    private String email;
    private String username;
    private Set<RefreshToken> roles;

}
