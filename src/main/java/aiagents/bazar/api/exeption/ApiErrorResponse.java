package aiagents.bazar.api.exeption;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class ApiErrorResponse {

    LocalDateTime timestamp;
    int status;
    String error;
    String message;
    String path;
    List<FieldError> fieldErrors;

    @Value
    @Builder
    public static class FieldError {
        String field;
        String message;
    }
}


