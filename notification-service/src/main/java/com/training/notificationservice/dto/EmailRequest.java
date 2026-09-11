package com.training.notificationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailRequest(

        @NotBlank(message = "Email получателя не должен быть пустым")
        @Email(message = "Email получателя должен быть корректным")
        String to,

        @NotBlank(message = "Тема сообщения не должна быть пустой")
        String subject,

        @NotBlank(message = "Текст сообщения не должен быть пустым")
        String message

) {
}