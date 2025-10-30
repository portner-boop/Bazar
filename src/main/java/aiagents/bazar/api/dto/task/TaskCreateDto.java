package aiagents.bazar.api.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateDto {
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
}
