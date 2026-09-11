package com.training.documentservice.dto;

import com.training.documentservice.entity.DocumentStatus;
import com.training.documentservice.entity.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DocumentUpdateRequest(
        @NotNull(message = "Тип документа не должен быть пустым")
        DocumentType type,

        @NotNull(message = "Статус документа не должен быть пустым")
        DocumentStatus status,

        @NotBlank(message = "Имя файла не должно быть пустым")
        @Size(max = 255, message = "Имя файла не должно превышать 255 символов")
        String fileName,

        @Size(max = 500, message = "Ключ хранилища не должен превышать 500 символов")
        String storageKey
) {
}
