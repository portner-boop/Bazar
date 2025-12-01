package aiagents.bazar.api.service;

import aiagents.bazar.api.dto.task.TaskResponseDto;
import aiagents.bazar.api.mapper.task.TaskMapper;
import aiagents.bazar.data.entity.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskQueryService {

    private final EntityManager entityManager;
    private final TaskMapper mapper;

    @Transactional
    public Page<TaskResponseDto> filter(String region, String status, Long categoryId, Long telegramUserId, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Task> countRoot = countQuery.from(Task.class);
        Predicate countPredicate = buildPredicate(cb, countRoot, region, status, categoryId, telegramUserId);
        countQuery.select(cb.count(countRoot)).where(countPredicate);
        Long total = entityManager.createQuery(countQuery).getSingleResult();
        CriteriaQuery<Task> dataQuery = cb.createQuery(Task.class);
        Root<Task> root = dataQuery.from(Task.class);
        root.fetch("category", jakarta.persistence.criteria.JoinType.LEFT);
        root.fetch("telegramUser", jakarta.persistence.criteria.JoinType.LEFT);
        root.fetch("images", jakarta.persistence.criteria.JoinType.LEFT); // Load images to avoid LazyInitializationException
        
        Predicate predicate = buildPredicate(cb, root, region, status, categoryId, telegramUserId);
        dataQuery.where(predicate);
        dataQuery.distinct(true);
        List<Task> results = entityManager.createQuery(dataQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        
        List<TaskResponseDto> content = results.stream()
                .map(mapper::toResponseDTO)
                .toList();
        
        return new PageImpl<>(content, pageable, total);
    }
    
    private Predicate buildPredicate(CriteriaBuilder cb, Root<Task> root, String region, String status, Long categoryId, Long telegramUserId) {
        Predicate predicate = cb.conjunction();
        if (region != null) predicate = cb.and(predicate, cb.equal(root.get("region"), region));
        if (status != null) predicate = cb.and(predicate, cb.equal(root.get("status"), status));
        if (categoryId != null)
            predicate = cb.and(predicate, cb.equal(root.get("category").get("id"), categoryId));
        if (telegramUserId != null)
            predicate = cb.and(predicate, cb.equal(root.get("telegramUser").get("id"), telegramUserId));
        return predicate;
    }
}
