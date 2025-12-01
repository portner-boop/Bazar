package aiagents.bazar.api.dto.claim;

import aiagents.bazar.data.entity.ClaimStatus;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class ClaimResponseDto {
    private Long id;
    private ClaimStatus status;
    private String message;
    private Long taskId;
    private Long telegramUserId;
    private LocalDateTime createdAt;
}
