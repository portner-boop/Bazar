package aiagents.bazar.api.dto.telegramuser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUserUpdateDto {

    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    @Email
    @Size(max = 255)
    private String email;
}
