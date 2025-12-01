package aiagents.bazar.api.service;

import aiagents.bazar.api.dto.telegramuser.TelegramUserResponseDto;
import aiagents.bazar.api.dto.telegramuser.TelegramUserUpdateDto;
import aiagents.bazar.api.dto.telegramuser.UserRoleUpdateDto;
import aiagents.bazar.api.exeption.telegramuser.NotFoundUserException;
import aiagents.bazar.api.exeption.telegramuser.UnauthorizedRoleException;
import aiagents.bazar.api.mapper.telegramuser.TelegramUserMapper;
import aiagents.bazar.data.entity.TelegramUser;
import aiagents.bazar.data.entity.UserRole;
import aiagents.bazar.data.repository.TelegramUserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Positive;
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
        if (user == null) {
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

    @Transactional
    public TelegramUserResponseDto getUserByTelegramId(@Positive Long telegramId) {
        TelegramUser user = telegramUserRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new NotFoundUserException("Not found user with id: " + telegramId));
        return telegramUserMapper.toResponseDTO(user);
    }

    @Transactional
    public TelegramUserResponseDto updateUserRole(Long adminId, Long targetUserId, UserRoleUpdateDto dto) {
        TelegramUser admin = telegramUserRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundUserException("Admin not found with id: " + adminId));

        if (admin.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedRoleException("Only ADMIN users can change roles");
        }

        TelegramUser targetUser = telegramUserRepository.findById(targetUserId)
                .orElseThrow(() -> new NotFoundUserException("Target user not found with id: " + targetUserId));

        // Prevent admin from changing another admin's role
        if (targetUser.getRole() == UserRole.ADMIN && !adminId.equals(targetUserId)) {
            throw new UnauthorizedRoleException("Cannot change role of another ADMIN user");
        }

        targetUser.setRole(dto.getRole());
        TelegramUser updated = telegramUserRepository.save(targetUser);
        return telegramUserMapper.toResponseDTO(updated);
    }

}
