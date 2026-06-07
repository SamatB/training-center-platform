package com.training.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "Имя обязательно")
    private String firstName;

    @NotBlank(message = "Фамилия обязательно")
    private String lastName;

    @Email(message = "Элоктронная почта должна быть действительной")
    @NotBlank(message = "Требуется электронная почта")
    private String email;

    @NotBlank(message = "Требуется пароль")
    @Size(min = 8, message = "Пароль должен содержать не менее 8 символов")
    private String password;
}
