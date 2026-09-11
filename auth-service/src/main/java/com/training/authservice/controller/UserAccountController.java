package com.training.authservice.controller;

import com.training.authservice.config.OpenApiConfig;
import com.training.authservice.dto.request.LoginRequest;
import com.training.authservice.dto.request.RegisterRequest;
import com.training.authservice.dto.request.UpdateProfileRequest;
import com.training.authservice.dto.response.AuthResponse;
import com.training.authservice.dto.response.ProfileUpdateResponse;
import com.training.authservice.dto.response.UserAccountResponse;
import com.training.authservice.exception.ErrorResponse;
import com.training.authservice.service.AuthService;
import com.training.authservice.service.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "Регистрация, вход и работа с текущей учётной записью")
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final AuthService authService;

    @Operation(summary = "Зарегистрировать нового пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные регистрации",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Пользователь с такой электронной почтой уже существует",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<UserAccountResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userAccountService.register(request));
    }

    @Operation(summary = "Выполнить вход и получить Access Token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Авторизация прошла успешно"),
            @ApiResponse(responseCode = "401", description = "Неверные учётные данные или аккаунт заблокирован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    @Operation(summary = "Получить профиль текущего пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль пользователя успешно получен"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/me")
    public ResponseEntity<UserAccountResponse> me() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }

    @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь успешно получен"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Требуется аутентификация",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserAccountResponse> getUserById(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(
                userAccountService.getUserById(userId)
        );
    }

    @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    @Operation(summary = "Обновить профиль текущего пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Указанная электронная почта уже используется",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/me")
    public ResponseEntity<ProfileUpdateResponse> updateMe(
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(authService.updateCurrentUser(request));
    }

    @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    @Operation(summary = "Удалить учётную запись текущего пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Учётная запись успешно удалена"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe() {
        authService.deleteCurrentUser();
        return ResponseEntity.noContent().build();
    }
}
