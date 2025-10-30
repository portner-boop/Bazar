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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramUserService {

    private final TelegramUserMapper telegramUserMapper;
    private final TelegramUserRepository telegramUserRepository;

    public boolean createUserIfNotExist(User user) {
        if(telegramUserRepository.existsByUserName(user.getUserName())){
            return false;
        }
        TelegramUser telegramUser = telegramUserMapper.toTelegramUser(user);
        telegramUserRepository.save(telegramUser);
        return true;
    }
    @Transactional
    public Flux<TelegramUserResponseDto> getAllUsers() {
        List<TelegramUser> users = telegramUserRepository.findAll();
        return Flux.fromIterable(users)
                .map(telegramUserMapper::toResponseDTO);
    }

    @Transactional
    public Mono<TelegramUserResponseDto> getUserById(Long id) {
        return Mono.fromCallable(() -> telegramUserRepository
                        .findById(id)
                        .map(telegramUserMapper::toResponseDTO)
                        .orElseThrow(() -> new NotFoundUserException("Not found user with id: " + id)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<TelegramUserResponseDto> updateUser(Long id, TelegramUserUpdateDto updateDto) {
        return Mono.fromCallable(() -> {
            TelegramUser user = telegramUserRepository.findById(id)
                    .orElseThrow(() -> new NotFoundUserException("Not found user with id: " + id));
            telegramUserMapper.updateFromDto(updateDto, user);
            TelegramUser updated = telegramUserRepository.save(user);
            return telegramUserMapper.toResponseDTO(updated);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
