package aiagents.bazar.api.dto.telegramuser;

import aiagents.bazar.data.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUserResponseDto {
    private Long id;
    private Long telegramId;
    private String userName;
    private String firstName;
    private String lastName;
    private String languageCode;
    private String email;
    private UserRole role;
}