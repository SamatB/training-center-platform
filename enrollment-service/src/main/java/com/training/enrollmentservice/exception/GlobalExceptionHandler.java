package com.training.enrollmentservice.exception;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EnrollmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EnrollmentNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException ex) {
        HttpStatus status;
        String message;

        if (ex.status() == 404) {
            status = HttpStatus.NOT_FOUND;
            message = "Запрашиваемый ресурс во внешнем сервисе не найден";

            log.warn(
                    "Внешний сервис вернул HTTP 404, причина: ресурс не найден"
            );
        } else if (ex.status() < 0 || ex.status() >= 500) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
            message = "Связанный сервис временно недоступен";

            log.error(
                    "Ошибка взаимодействия с внешним сервисом, HTTP status={}",
                    ex.status()
            );
        } else {
            status = HttpStatus.BAD_GATEWAY;
            message = "Не удалось получить корректный ответ от связанного сервиса";

            log.error(
                    "Внешний сервис вернул неожиданный HTTP status={}",
                    ex.status()
            );
        }

        ErrorResponse error = new ErrorResponse(
                message,
                status.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorResponse error = new ErrorResponse(
                message,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(EnrollmentAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExists(
            EnrollmentAlreadyExistsException ex
    ) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(DownstreamServiceException.class)
    public ResponseEntity<ErrorResponse> handleDownstreamServiceException(
            DownstreamServiceException ex
    ) {
        HttpStatus status;
        String message;

        if (ex.getStatus() == 404) {
            status = HttpStatus.NOT_FOUND;
            message = ex.getMessage();

            log.warn(
                    "Сервис {} не нашёл ресурс id={}, HTTP status={}, причина={}",
                    ex.getServiceName(),
                    ex.getResourceId(),
                    ex.getStatus(),
                    message
            );
        } else if (ex.getStatus() < 0 || ex.getStatus() >= 500) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
            message = "Связанный сервис временно недоступен";

            log.error(
                    "Ошибка взаимодействия с сервисом {}: resource id={}, HTTP status={}, причина={}",
                    ex.getServiceName(),
                    ex.getResourceId(),
                    ex.getStatus(),
                    ex.getMessage()
            );
        } else {
            status = HttpStatus.BAD_GATEWAY;
            message = "Не удалось получить корректный ответ от связанного сервиса";

            log.error(
                    "Неожиданный ответ сервиса {}: resource id={}, HTTP status={}, причина={}",
                    ex.getServiceName(),
                    ex.getResourceId(),
                    ex.getStatus(),
                    ex.getMessage()
            );
        }

        ErrorResponse error = new ErrorResponse(
                message,
                status.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(error);
    }
}
