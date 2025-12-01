package aiagents.bazar.api.controller;

import aiagents.bazar.api.dto.task.TaskImageResponseDto;
import aiagents.bazar.api.service.TaskImageService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
public class TaskImageController {

    private final TaskImageService taskImageService;

    @PostMapping("/{taskId}/images")
    public ResponseEntity<List<TaskImageResponseDto>> uploadImages(
            @PathVariable @Positive Long taskId,
            @RequestParam("files") MultipartFile[] files
    ) {
        List<TaskImageResponseDto> uploadedImages = taskImageService.uploadImages(taskId, files);
        return ResponseEntity.created(URI.create("/api/v1/tasks/" + taskId + "/images"))
                .body(uploadedImages);
    }

    @GetMapping("/{taskId}/images")
    public ResponseEntity<List<TaskImageResponseDto>> getImages(
            @PathVariable @Positive Long taskId
    ) {
        return ResponseEntity.ok(taskImageService.getImagesByTaskId(taskId));
    }

    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable @Positive Long imageId
    ) {
        taskImageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}

