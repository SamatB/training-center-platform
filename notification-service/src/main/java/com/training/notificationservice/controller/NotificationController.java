package com.training.notificationservice.controller;

import com.training.notificationservice.dto.NotificationCreateRequest;
import com.training.notificationservice.dto.NotificationResponse;
import com.training.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.training.notificationservice.dto.EmailRequest;

import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(
        name = "Notifications",
        description = "Operations for creating and retrieving notifications"
)
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create notification",
            description = "Creates a new notification with PENDING status"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Notification successfully created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            )
    })
    public NotificationResponse create(
            @Valid @RequestBody NotificationCreateRequest request
    ) {
        return notificationService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get notification by ID",
            description = "Returns notification data by its UUID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Notification successfully found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid notification UUID"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Notification not found"
            )
    })
    public NotificationResponse getById(@PathVariable UUID id) {

        return notificationService.getById(id);
    }
    @PostMapping("/email")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Send email",
            description = "Sends an email notification"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Email sent successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Failed to send email"
            )
    })
    public void sendEmail(
            @Valid @RequestBody EmailRequest request
    ) {
        notificationService.sendEmail(
                request.to(),
                request.subject(),
                request.message()
        );
    }

}
