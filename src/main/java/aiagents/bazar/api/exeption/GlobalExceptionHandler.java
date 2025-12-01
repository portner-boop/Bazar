package aiagents.bazar.api.exeption;

import aiagents.bazar.api.exeption.telegramuser.NotFoundUserException;
import aiagents.bazar.api.exeption.telegramuser.UnauthorizedRoleException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> ApiErrorResponse.FieldError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        ApiErrorResponse body = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed")
                .path(request.getRequestURI())
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        var fieldErrors = ex.getConstraintViolations().stream()
                .map(violation -> ApiErrorResponse.FieldError.builder()
                        .field(violation.getPropertyPath().toString())
                        .message(violation.getMessage())
                        .build())
                .collect(Collectors.toList());

        ApiErrorResponse body = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed")
                .path(request.getRequestURI())
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundUser(
            NotFoundUserException ex,
            HttpServletRequest request
    ) {
        ApiErrorResponse body = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(UnauthorizedRoleException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorizedRole(
            UnauthorizedRoleException ex,
            HttpServletRequest request
    ) {
        ApiErrorResponse body = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        String message = ex.getMessage();
        
        // Обработка ошибок десериализации enum
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) ex.getCause();
            if (ife.getTargetType() != null && ife.getTargetType().isEnum()) {
                String enumName = ife.getTargetType().getSimpleName();
                String invalidValue = ife.getValue() != null ? ife.getValue().toString() : "null";
                String fieldName = ife.getPath().stream()
                        .map(ref -> ref.getFieldName())
                        .reduce((first, second) -> second)
                        .orElse("unknown");
                
                message = String.format(
                    "Invalid value '%s' for field '%s' of type %s. Valid values are: %s",
                    invalidValue,
                    fieldName,
                    enumName,
                    getEnumValues(ife.getTargetType())
                );
            }
        }
        
        ApiErrorResponse body = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
    
    private String getEnumValues(Class<?> enumClass) {
        if (!enumClass.isEnum()) {
            return "";
        }
        Object[] enumConstants = enumClass.getEnumConstants();
        if (enumConstants == null) {
            return "";
        }
        return java.util.Arrays.stream(enumConstants)
                .map(Object::toString)
                .collect(java.util.stream.Collectors.joining(", "));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        String message = ex.getMessage();
        
        // Обработка ошибок enum из базы данных (например, "No enum constant ...")
        if (message != null && message.contains("No enum constant")) {
            // Извлекаем информацию из сообщения типа: "No enum constant aiagents.bazar.data.entity.EscrowStatus.acitve"
            String[] parts = message.split("\\.");
            if (parts.length > 0) {
                String enumName = parts[parts.length - 2]; // EscrowStatus
                String invalidValue = parts[parts.length - 1]; // acitve
                
                // Пытаемся найти класс enum
                try {
                    String fullEnumClassName = message.substring(message.indexOf("enum constant") + 14, message.lastIndexOf("."));
                    Class<?> enumClass = Class.forName(fullEnumClassName);
                    if (enumClass.isEnum()) {
                        message = String.format(
                            "Invalid enum value '%s' for type %s found in database. Valid values are: %s. Please update the database record.",
                            invalidValue,
                            enumName,
                            getEnumValues(enumClass)
                        );
                    }
                } catch (ClassNotFoundException e) {
                    // Если не удалось найти класс, оставляем оригинальное сообщение
                }
            }
        }
        
        ApiErrorResponse body = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ) {
        ApiErrorResponse body = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}


