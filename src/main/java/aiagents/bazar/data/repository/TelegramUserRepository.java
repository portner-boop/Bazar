package aiagents.bazar.data.repository;

import aiagents.bazar.data.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {

    boolean existsByTelegramId(Long telegramId);

    Optional<TelegramUser> findByTelegramId(Long telegramId);
}
