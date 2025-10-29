package aiagents.bazar.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;

@Configuration
public class TelegramConfig {

    @Value("${bot.token}")
    private String token;

    @Bean
    public OkHttpTelegramClient telegramClient() {
        return new OkHttpTelegramClient(token);
    }
}
