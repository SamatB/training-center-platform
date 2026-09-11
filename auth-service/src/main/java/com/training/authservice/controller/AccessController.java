package com.training.authservice.controller;

import com.training.authservice.config.OpenApiConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/access")
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@Tag(
        name = "Контроль доступа",
        description = "Эндпоинты для демонстрации ролевого доступа"
)
public class AccessController {

    @GetMapping("/student")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Проверить доступ уровня студента")
    public ResponseEntity<String> studentAccess() {
        return ResponseEntity.ok("Доступ студента разрешён");
    }

    @GetMapping("/teacher")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Проверить доступ уровня преподавателя")
    public ResponseEntity<String> teacherAccess() {
        return ResponseEntity.ok("Доступ преподавателя разрешён");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Проверить доступ уровня администратора")
    public ResponseEntity<String> adminAccess() {
        return ResponseEntity.ok("Доступ администратора разрешён");
    }
}
