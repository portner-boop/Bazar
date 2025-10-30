package aiagents.bazar.api.dto.claim;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimCreateDto {
    private Long taskId;
    private Long telegramUserId;
    private String message;
}
