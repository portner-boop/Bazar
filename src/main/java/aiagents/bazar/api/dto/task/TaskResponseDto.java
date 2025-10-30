package aiagents.bazar.api.dto.task;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private String region;
    private BigDecimal priceExpected;
    private BigDecimal rewardAmount;
    private BigDecimal rewardPercentage;
    private String rewardType;
    private String status;
    private String escrowStatus;
    private Long categoryId;
    private Long telegramUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
