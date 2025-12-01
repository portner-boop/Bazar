package aiagents.bazar.api.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskImageResponseDto {
    private Long id;
    private String url;
    private Integer sortOrder;
}

