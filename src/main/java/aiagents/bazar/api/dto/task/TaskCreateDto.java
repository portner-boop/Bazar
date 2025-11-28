package aiagents.bazar.api.dto.task;

import aiagents.bazar.data.entity.EscrowStatus;
import aiagents.bazar.data.entity.RewardType;
import aiagents.bazar.data.entity.TaskStatus;
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
    private RewardType rewardType;
    private TaskStatus status;
    private EscrowStatus escrowStatus;
    private Long categoryId;
    private Long telegramUserId;
}
