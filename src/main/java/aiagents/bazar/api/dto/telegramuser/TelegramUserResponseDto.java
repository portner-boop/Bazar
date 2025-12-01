package aiagents.bazar.api.dto.telegramuser;

import aiagents.bazar.data.entity.UserRole;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
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