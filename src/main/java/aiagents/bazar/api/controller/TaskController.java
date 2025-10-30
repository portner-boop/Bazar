package aiagents.bazar.api.controller;

import aiagents.bazar.api.dto.task.TaskCreateDto;
import aiagents.bazar.api.dto.task.TaskResponseDto;
import aiagents.bazar.api.dto.task.TaskUpdateDto;
import aiagents.bazar.api.service.TaskQueryService;
import aiagents.bazar.api.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;
    private final TaskQueryService queryService;

    @PostMapping
    public Mono<TaskResponseDto> create(@RequestBody TaskCreateDto dto) {
        return service.createTask(dto);
    }

    @GetMapping
    public Flux<TaskResponseDto> getAll() {
        return service.getAllTasks();
    }

    @GetMapping("/{id}")
    public Mono<TaskResponseDto> getById(@PathVariable Long id) {
        return service.getTaskById(id);
    }

    @PutMapping("/{id}")
    public Mono<TaskResponseDto> update(@PathVariable Long id, @RequestBody TaskUpdateDto dto) {
        return service.updateTask(id, dto);
    }

    @GetMapping("/filter")
    public Flux<TaskResponseDto> filter(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long telegramUserId
    ) {
        return queryService.filter(region, status, categoryId, telegramUserId);
    }
}
