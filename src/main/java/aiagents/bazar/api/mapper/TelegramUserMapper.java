package aiagents.bazar.api.mapper;

import aiagents.bazar.data.entity.TelegramUser;
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
        return telegramUser;
    }
}
