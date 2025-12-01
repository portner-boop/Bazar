package aiagents.bazar.api.dto.telegramuser;

import aiagents.bazar.data.entity.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleUpdateDto {
    @NotNull(message = "Role is required")
    private UserRole role;
}

