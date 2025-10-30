package aiagents.bazar.api.dto.category;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateDto {
    private String name;
    private String description;
}
