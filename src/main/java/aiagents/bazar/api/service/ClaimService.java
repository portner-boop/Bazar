package aiagents.bazar.api.service;

import aiagents.bazar.api.dto.claim.ClaimCreateDto;
import aiagents.bazar.api.dto.claim.ClaimResponseDto;
import aiagents.bazar.api.dto.claim.ClaimUpdateDto;
import aiagents.bazar.api.mapper.Claim.ClaimMapper;
import aiagents.bazar.data.entity.Claim;
import aiagents.bazar.data.entity.Task;
import aiagents.bazar.data.entity.TelegramUser;
import aiagents.bazar.data.repository.ClaimRepository;
import aiagents.bazar.data.repository.TaskRepository;
import aiagents.bazar.data.repository.TelegramUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final TaskRepository taskRepository;
    private final TelegramUserRepository telegramUserRepository;
    private final ClaimMapper mapper;
    private final EntityManager entityManager;

    @Transactional
    public ClaimResponseDto create(ClaimCreateDto dto) {
        Task task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));
        TelegramUser user = telegramUserRepository.findById(dto.getTelegramUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Claim claim = mapper.fromCreateDto(dto, task, user);
        Claim saved = claimRepository.save(claim);
        return mapper.toResponseDto(saved);
    }

    @Transactional
    public Page<ClaimResponseDto> getAll(String status, Long taskId, Long telegramUserId, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Claim> countRoot = countQuery.from(Claim.class);
        Predicate countPredicate = buildPredicate(cb, countRoot, status, taskId, telegramUserId);
        countQuery.select(cb.count(countRoot)).where(countPredicate);
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        CriteriaQuery<Claim> dataQuery = cb.createQuery(Claim.class);
        Root<Claim> root = dataQuery.from(Claim.class);

        root.fetch("task", jakarta.persistence.criteria.JoinType.LEFT);
        root.fetch("telegramUser", jakarta.persistence.criteria.JoinType.LEFT);
        
        Predicate predicate = buildPredicate(cb, root, status, taskId, telegramUserId);
        dataQuery.where(predicate);
        dataQuery.distinct(true);
        List<Claim> results = entityManager.createQuery(dataQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        
        List<ClaimResponseDto> content = results.stream()
                .map(mapper::toResponseDto)
                .toList();
        
        return new PageImpl<>(content, pageable, total);
    }
    
    private Predicate buildPredicate(CriteriaBuilder cb, Root<Claim> root, String status, Long taskId, Long telegramUserId) {
        Predicate predicate = cb.conjunction();
        if (status != null) predicate = cb.and(predicate, cb.equal(root.get("status"), status));
        if (taskId != null) predicate = cb.and(predicate, cb.equal(root.get("task").get("id"), taskId));
        if (telegramUserId != null)
            predicate = cb.and(predicate, cb.equal(root.get("telegramUser").get("id"), telegramUserId));
        return predicate;
    }

    @Transactional
    public ClaimResponseDto getById(Long id) {
        return claimRepository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException("Claim not found"));
    }

    @Transactional
    public ClaimResponseDto update(Long id, ClaimUpdateDto dto) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found"));
        mapper.updateFromDto(dto, claim);
        Claim updated = claimRepository.save(claim);
        return mapper.toResponseDto(updated);
    }

    @Transactional
    public void delete(Long id) {
        claimRepository.deleteById(id);
    }
}
