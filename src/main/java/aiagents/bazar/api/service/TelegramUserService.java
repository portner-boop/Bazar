package aiagents.bazar.api.service;

import aiagents.bazar.api.dto.telegramuser.TelegramUserResponseDto;
import aiagents.bazar.api.dto.telegramuser.TelegramUserUpdateDto;
import aiagents.bazar.api.exeption.telegramuser.NotFoundUserException;
import aiagents.bazar.api.mapper.telegramuser.TelegramUserMapper;
import aiagents.bazar.data.entity.TelegramUser;
import aiagents.bazar.data.repository.TelegramUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramUserService {

    private final TelegramUserMapper telegramUserMapper;
    private final TelegramUserRepository telegramUserRepository;

    @Transactional
    public boolean createUserIfNotExist(User user) {
        if (user == null || user.getId() == null) {
            return false;
        }
        if (telegramUserRepository.existsByTelegramId(user.getId())) {
            return false;
        }
        TelegramUser telegramUser = telegramUserMapper.toTelegramUser(user);
        telegramUserRepository.save(telegramUser);
        return true;
    }
    @Transactional
    public List<TelegramUserResponseDto> getAllUsers() {
        return telegramUserRepository.findAll().stream()
                .map(telegramUserMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public TelegramUserResponseDto getUserById(Long id) {
        return telegramUserRepository
                .findById(id)
                .map(telegramUserMapper::toResponseDTO)
                .orElseThrow(() -> new NotFoundUserException("Not found user with id: " + id));
    }

    @Transactional
    public TelegramUserResponseDto updateUser(Long id, TelegramUserUpdateDto updateDto) {
        TelegramUser user = telegramUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundUserException("Not found user with id: " + id));
        telegramUserMapper.updateFromDto(updateDto, user);
        TelegramUser updated = telegramUserRepository.save(user);
        return telegramUserMapper.toResponseDTO(updated);
    }
}
