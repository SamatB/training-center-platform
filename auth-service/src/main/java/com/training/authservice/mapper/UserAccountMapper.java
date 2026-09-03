package com.training.authservice.mapper;

import com.training.authservice.dto.request.RegisterRequest;
import com.training.authservice.dto.response.UserAccountResponse;
import com.training.authservice.entity.UserAccount;
import org.springframework.stereotype.Component;

@Component
public class UserAccountMapper {

    public UserAccount toEntity(RegisterRequest request) {

        if (request == null){
            return null;
        }

        return UserAccount.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .enabled(true)
                .build();
    }

    public UserAccountResponse toResponse(UserAccount userAccount) {

        if (userAccount == null) {
            return null;
        }

        return UserAccountResponse.builder()
                .id(userAccount.getId())
                .firstName(userAccount.getFirstName())
                .lastName(userAccount.getLastName())
                .email(userAccount.getEmail())
                .role(userAccount.getRole())
                .enabled(userAccount.getEnabled())
                .createdAt(userAccount.getCreatedAt())
                .updatedAt(userAccount.getUpdatedAt())
                .build();
    }
}
