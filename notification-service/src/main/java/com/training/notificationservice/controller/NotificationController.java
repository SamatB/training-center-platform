package com.training.notificationservice.controller;
import com.training.notificationservice.dto.NotificationCreateRequest;
import com.training.notificationservice.dto.NotificationResponse;
import com.training.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationResponse create(
            @RequestBody NotificationCreateRequest request
            ) {
        return notificationService.create(request);
    }

        @GetMapping("/{id}")
    public NotificationResponse getById(@PathVariable UUID id){

        return notificationService.getById(id);

    }
}
