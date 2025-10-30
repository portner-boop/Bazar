package aiagents.bazar.api.service;

import aiagents.bazar.api.dto.task.TaskCreateDto;
import aiagents.bazar.api.dto.task.TaskResponseDto;
import aiagents.bazar.api.dto.task.TaskUpdateDto;
import aiagents.bazar.api.exeption.NotFoundTaskException;
import aiagents.bazar.api.mapper.task.TaskMapper;
import aiagents.bazar.data.entity.Category;
import aiagents.bazar.data.entity.Task;
import aiagents.bazar.data.entity.TelegramUser;
import aiagents.bazar.data.repository.CategoryRepository;
import aiagents.bazar.data.repository.TaskRepository;
import aiagents.bazar.data.repository.TelegramUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final TelegramUserRepository telegramUserRepository;
    private final TaskMapper mapper;

    @Transactional
    public Mono<TaskResponseDto> createTask(TaskCreateDto dto) {
        return Mono.fromCallable(() -> {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            TelegramUser creator = telegramUserRepository.findById(dto.getTelegramUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Task task = new Task();
            task.setTitle(dto.getTitle());
            task.setDescription(dto.getDescription());
            task.setRegion(dto.getRegion());
            task.setPriceExpected(dto.getPriceExpected());
            task.setRewardAmount(dto.getRewardAmount());
            task.setRewardPercentage(dto.getRewardPercentage());
            task.setRewardType(dto.getRewardType());
            task.setStatus(dto.getStatus());
            task.setEscrowStatus(dto.getEscrowStatus());
            task.setCategory(category);
            task.setTelegramUser(creator);

            Task saved = taskRepository.save(task);
            return mapper.toResponseDTO(saved);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Flux<TaskResponseDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return Flux.fromIterable(tasks)
                .map(mapper::toResponseDTO);
    }

    @Transactional
    public Mono<TaskResponseDto> getTaskById(Long id) {
        return Mono.fromCallable(() -> taskRepository.findById(id)
                        .map(mapper::toResponseDTO)
                        .orElseThrow(() -> new NotFoundTaskException("Task not found with id: " + id)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<TaskResponseDto> updateTask(Long id, TaskUpdateDto dto) {
        return Mono.fromCallable(() -> {
            Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new NotFoundTaskException("Task not found with id: " + id));
            mapper.updateFromDto(dto, task);
            Task updated = taskRepository.save(task);
            return mapper.toResponseDTO(updated);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
