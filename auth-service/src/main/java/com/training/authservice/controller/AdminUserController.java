package com.training.authservice.controller;

import com.training.authservice.config.OpenApiConfig;
import com.training.authservice.dto.request.ChangeUserRoleRequest;
import com.training.authservice.dto.request.ChangeUserStatusRequest;
import com.training.authservice.dto.response.DeletedUsersResponse;
import com.training.authservice.dto.response.UserRoleResponse;
import com.training.authservice.dto.response.UserStatusResponse;
import com.training.authservice.dto.response.UserSummaryResponse;
import com.training.authservice.entity.Role;
import com.training.authservice.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@Tag(name = "Администрирование", description = "Управление пользователями")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Operation(summary = "Изменить роль пользователя")
    @PatchMapping("/{userId}/role")
    public ResponseEntity<UserRoleResponse> changeRole(
            @PathVariable UUID userId,
            @Valid @RequestBody ChangeUserRoleRequest request
    ) {
        return ResponseEntity.ok(adminUserService.changeRole(userId, request));
    }

    @Operation(summary = "Получить список пользователей с пагинацией, сортировкой и фильтром по роли")
    @GetMapping
    public ResponseEntity<Page<UserSummaryResponse>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) Role role
    ) {
        return ResponseEntity.ok(adminUserService.getUsers(page, size, sortBy, direction, role));
    }

    @Operation(summary = "Получить пользователя по id")
    @GetMapping("/{userId}")
    public ResponseEntity<UserSummaryResponse> getUserById(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(
                adminUserService.getUserById(userId)
        );
    }

    @Operation(summary = "Удалить пользователя по id")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        adminUserService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Удалить всех пользователей с ролью STUDENT")
    @DeleteMapping("/students")
    public ResponseEntity<DeletedUsersResponse> deleteAllStudents() {
        return ResponseEntity.ok(adminUserService.deleteAllStudents());
    }

    @Operation(summary = "Заблокировать или разблокировать пользователя")
    @PatchMapping("/{userId}/status")
    public ResponseEntity<UserStatusResponse> changeStatus(
            @PathVariable UUID userId,
            @Valid @RequestBody ChangeUserStatusRequest request
    ) {
        return ResponseEntity.ok(adminUserService.changeStatus(userId, request));
    }
}
