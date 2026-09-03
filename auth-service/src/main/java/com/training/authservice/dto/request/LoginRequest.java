package com.training.authservice.dto.request;

import com.training.authservice.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @Email(message = "Элоктронная почта должна быть действительной")
    @NotBlank(message = "Требуется электронная почта")
    private String email;

    @NotBlank(message = "Требуется пароль")
    @Size(
            min = 8,
            max = 100,
            message = "Пароль должен содержать от 8 до 100 символов"
    )    private String password;
}