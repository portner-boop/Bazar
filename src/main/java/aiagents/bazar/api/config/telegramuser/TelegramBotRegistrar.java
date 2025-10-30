package aiagents.bazar.api.config.telegramuser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
public class TelegramBotRegistrar {
    @Value("${bot.token}")
    private String botToken;
    @Bean
    public TelegramBotsLongPollingApplication registerBot(LongPollingSingleThreadUpdateConsumer consumer)
            throws TelegramApiException {
        TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication();
        app.registerBot(botToken, consumer);
        return app;
    }
}