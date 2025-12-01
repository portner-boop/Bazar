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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final TelegramUserRepository telegramUserRepository;
    private final TaskMapper mapper;

    @Transactional
    @CacheEvict(value = {"tasks", "task"}, allEntries = true)
    public TaskResponseDto createTask(TaskCreateDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        TelegramUser creator = telegramUserRepository.findById(dto.getTelegramUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = mapper.fromCreateDto(dto, category, creator);

        Task saved = taskRepository.save(task);
        return mapper.toResponseDTO(saved);
    }

    @Transactional
    @Cacheable(value = "tasks", key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<TaskResponseDto> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(mapper::toResponseDTO);
    }

    @Transactional
    @Cacheable(value = "task", key = "#id")
    public TaskResponseDto getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(mapper::toResponseDTO)
                .orElseThrow(() -> new NotFoundTaskException("Task not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = {"tasks", "task"}, allEntries = true)
    public TaskResponseDto updateTask(Long id, TaskUpdateDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundTaskException("Task not found with id: " + id));
        mapper.updateFromDto(dto, task);
        Task updated = taskRepository.save(task);
        return mapper.toResponseDTO(updated);
    }
}
