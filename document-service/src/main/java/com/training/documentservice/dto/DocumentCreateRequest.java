package com.training.documentservice.dto;

import com.training.documentservice.entity.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record DocumentCreateRequest(
        @NotNull(message = "ID пользователя не должен быть пустым")
        UUID userId,

        UUID enrollmentId,

        UUID paymentId,

        @NotNull(message = "Тип документа не должен быть пустым")
        DocumentType type,

        @NotBlank(message = "Имя файла не должно быть пустым")
        @Size(max = 255, message = "Имя файла не должно превышать 255 символов")
        String fileName,

        @Size(max = 500, message = "Ключ хранилища не должен превышать 500 символов")
        String storageKey
) {
}
