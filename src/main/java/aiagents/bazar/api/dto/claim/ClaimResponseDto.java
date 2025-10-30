package aiagents.bazar.api.dto.claim;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimResponseDto {
    private Long id;
    private String status;
    private String message;
    private Long taskId;
    private Long telegramUserId;
    private LocalDateTime createdAt;
}
