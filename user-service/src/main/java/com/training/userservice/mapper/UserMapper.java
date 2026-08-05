package com.training.userservice.mapper;

import com.training.userservice.dto.request.UserRequest;
import com.training.userservice.dto.response.UserResponse;
import com.training.userservice.entity.User;
import com.training.userservice.entity.UserStatus;
import jakarta.persistence.EnumType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class UserMapper {

    public User toEntity(UserRequest request) {
        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return user;
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role("user")
                .active(true)
                .build();
    }
}
