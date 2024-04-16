package fr.springsecurityjwtapp.spring_jwt_app.mapper;

import fr.springsecurityjwtapp.spring_jwt_app.dto.response.UserResponse;
import fr.springsecurityjwtapp.spring_jwt_app.entity.UserEntity;

public class UserMapper {

    public UserResponse toDTO(UserEntity userEntity) {
        UserResponse dto = new UserResponse();
        dto.setId(Long.valueOf(userEntity.getId()));
        dto.setEmail(userEntity.getEmail());
        return dto;
    }
}