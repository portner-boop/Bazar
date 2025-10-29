package aiagents.bazar.api.mapper;

import aiagents.bazar.api.dto.TelegramUserResponseDto;
import aiagents.bazar.api.dto.TelegramUserUpdateDto;
import aiagents.bazar.data.entity.TelegramUser;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TelegramUserMapStructMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "telegramId", target = "telegramId")
    @Mapping(source = "userName", target = "userName")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "languageCode", target = "languageCode")
    @Mapping(source = "email", target = "email")
    TelegramUserResponseDto toResponseDTO(TelegramUser entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(TelegramUserUpdateDto dto, @MappingTarget TelegramUser entity);}


