package aiagents.bazar.api.mapper.telegramuser;

import aiagents.bazar.api.dto.telegramuser.TelegramUserResponseDto;
import aiagents.bazar.api.dto.telegramuser.TelegramUserUpdateDto;
import aiagents.bazar.data.entity.TelegramUser;
import aiagents.bazar.data.entity.UserRole;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class TelegramUserMapper {

    public TelegramUser toTelegramUser(User user) {
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setFirstName(user.getFirstName());
        telegramUser.setLastName(user.getLastName());
        telegramUser.setUserName(user.getUserName());
        telegramUser.setTelegramId(user.getId());
        telegramUser.setLanguageCode(user.getLanguageCode());
        telegramUser.setRole(UserRole.USER);
        return telegramUser;
    }

    public TelegramUserResponseDto toResponseDTO(TelegramUser user) {
        if (user == null) return null;

        TelegramUserResponseDto dto = new TelegramUserResponseDto();
        dto.setId(user.getId());
        dto.setTelegramId(user.getTelegramId());
        dto.setUserName(user.getUserName());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setLanguageCode(user.getLanguageCode());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    public void updateFromDto(TelegramUserUpdateDto dto, TelegramUser user) {
        if (dto == null || user == null) return;

        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
    }
}
