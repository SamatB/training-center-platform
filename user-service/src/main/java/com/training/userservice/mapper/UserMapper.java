package com.training.userservice.mapper;

import com.training.userservice.dto.request.UserRequest;
import com.training.userservice.dto.response.UserResponse;
import com.training.userservice.entity.User;

public class UserMapper {

    public User toEntity(UserRequest request) {
        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return user;
    }

    public UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setEmail(user.getEmail());

        return response;
    }
}
