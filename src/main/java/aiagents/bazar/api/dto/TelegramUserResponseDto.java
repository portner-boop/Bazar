package aiagents.bazar.api.dto;

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
}