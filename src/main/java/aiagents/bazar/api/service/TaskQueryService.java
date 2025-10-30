package aiagents.bazar.api.service;

import aiagents.bazar.api.dto.task.TaskResponseDto;
import aiagents.bazar.api.mapper.task.TaskMapper;
import aiagents.bazar.data.entity.Task;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskQueryService {

    private final EntityManager entityManager;
    private final TaskMapper mapper;

    public Flux<TaskResponseDto> filter(String region, String status, Long categoryId, Long telegramUserId) {
        return Mono.fromCallable(() -> {
                    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                    CriteriaQuery<Task> cq = cb.createQuery(Task.class);
                    Root<Task> root = cq.from(Task.class);

                    Predicate predicate = cb.conjunction();
                    if (region != null) predicate = cb.and(predicate, cb.equal(root.get("region"), region));
                    if (status != null) predicate = cb.and(predicate, cb.equal(root.get("status"), status));
                    if (categoryId != null) predicate = cb.and(predicate, cb.equal(root.get("category").get("id"), categoryId));
                    if (telegramUserId != null) predicate = cb.and(predicate, cb.equal(root.get("telegramUser").get("id"), telegramUserId));

                    cq.where(predicate);
                    List<Task> results = entityManager.createQuery(cq).getResultList();
                    return results;
                }).flatMapMany(tasks -> Flux.fromIterable(tasks).map(mapper::toResponseDTO))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
