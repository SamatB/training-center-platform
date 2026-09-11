package com.training.userservice.controller;

import com.training.userservice.dto.response.UserResponse;
import com.training.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "API для получения профилей пользователей")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить профиль пользователя по id")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        log.info("Получен запрос на получение профиля пользователя id={}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    @Operation(summary = "Получить список профилей пользователей")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("Получен запрос на получение списка профилей пользователей");
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
