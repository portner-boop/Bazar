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
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final TaskRepository taskRepository;
    private final TelegramUserRepository telegramUserRepository;
    private final ClaimMapper mapper;
    private final EntityManager entityManager;

    @Transactional
    public Mono<ClaimResponseDto> create(ClaimCreateDto dto) {
        return Mono.fromCallable(() -> {
            Task task = taskRepository.findById(dto.getTaskId())
                    .orElseThrow(() -> new RuntimeException("Task not found"));
            TelegramUser user = telegramUserRepository.findById(dto.getTelegramUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Claim claim = new Claim();
            claim.setStatus("PENDING");
            claim.setMessage(dto.getMessage());
            claim.setTask(task);
            claim.setTelegramUser(user);

            Claim saved = claimRepository.save(claim);
            return mapper.toResponseDto(saved);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Flux<ClaimResponseDto> getAll(String status, Long taskId, Long telegramUserId) {
        return Mono.fromCallable(() -> {
                    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                    CriteriaQuery<Claim> cq = cb.createQuery(Claim.class);
                    Root<Claim> root = cq.from(Claim.class);

                    Predicate predicate = cb.conjunction();
                    if (status != null) predicate = cb.and(predicate, cb.equal(root.get("status"), status));
                    if (taskId != null) predicate = cb.and(predicate, cb.equal(root.get("task").get("id"), taskId));
                    if (telegramUserId != null) predicate = cb.and(predicate, cb.equal(root.get("telegramUser").get("id"), telegramUserId));

                    cq.where(predicate);
                    return entityManager.createQuery(cq).getResultList();
                }).flatMapMany(claims -> Flux.fromIterable(claims)
                        .map(mapper::toResponseDto))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<ClaimResponseDto> getById(Long id) {
        return Mono.fromCallable(() -> claimRepository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException("Claim not found")));
    }

    @Transactional
    public Mono<ClaimResponseDto> update(Long id, ClaimUpdateDto dto) {
        return Mono.fromCallable(() -> {
            Claim claim = claimRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Claim not found"));
            mapper.updateFromDto(dto, claim);
            Claim updated = claimRepository.save(claim);
            return mapper.toResponseDto(updated);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<Void> delete(Long id) {
        return Mono.fromRunnable(() -> claimRepository.deleteById(id))
                .subscribeOn(Schedulers.boundedElastic()).then();
    }
}
