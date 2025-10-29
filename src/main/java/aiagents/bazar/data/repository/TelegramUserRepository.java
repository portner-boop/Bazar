package aiagents.bazar.data.repository;

import aiagents.bazar.data.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {

    boolean existsByUserName(String userName);

}
