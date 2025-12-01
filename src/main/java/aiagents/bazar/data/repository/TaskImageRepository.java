package aiagents.bazar.data.repository;

import aiagents.bazar.data.entity.TaskImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskImageRepository extends JpaRepository<TaskImage, Long> {
    List<TaskImage> findByTaskIdOrderBySortOrderAsc(Long taskId);
    void deleteByTaskId(Long taskId);
}

