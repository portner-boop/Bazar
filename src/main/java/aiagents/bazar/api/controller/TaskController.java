package aiagents.bazar.api.controller;

import aiagents.bazar.api.dto.task.TaskCreateDto;
import aiagents.bazar.api.dto.task.TaskResponseDto;
import aiagents.bazar.api.dto.task.TaskUpdateDto;
import aiagents.bazar.api.service.TaskQueryService;
import aiagents.bazar.api.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService service;
    private final TaskQueryService queryService;

    @PostMapping
    public ResponseEntity<TaskResponseDto> create(@Valid @RequestBody TaskCreateDto dto) {
        TaskResponseDto created = service.createTask(dto);
        return ResponseEntity.created(URI.create("/api/v1/tasks/" + created.getId()))
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(service.getTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> update(@PathVariable @Positive Long id,
                                                  @Valid @RequestBody TaskUpdateDto dto) {
        return ResponseEntity.ok(service.updateTask(id, dto));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TaskResponseDto>> filter(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long telegramUserId
    ) {
        return ResponseEntity.ok(queryService.filter(region, status, categoryId, telegramUserId));
    }
}
