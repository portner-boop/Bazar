package aiagents.bazar.api.config;

import aiagents.bazar.api.mapper.TelegramUserMapStructMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public TelegramUserMapStructMapper telegramUserMapStructMapper() {
        return Mappers.getMapper(TelegramUserMapStructMapper.class);
    }
}
