package aiagents.bazar.data.repository;

import aiagents.bazar.data.entity.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    @EntityGraph(attributePaths = {"images", "category", "telegramUser"})
    Optional<Task> findById(Long id);
}
