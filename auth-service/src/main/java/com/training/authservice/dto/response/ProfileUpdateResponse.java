package com.training.authservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ после обновления профиля")
public record ProfileUpdateResponse(
        @Schema(description = "Обновлённые данные пользователя")
        UserAccountResponse user,

        @Schema(description = "Новый Access Token, содержащий актуальный email")
        String accessToken,

        @Schema(description = "Тип токена", example = "Bearer")
        String tokenType
) {
}
