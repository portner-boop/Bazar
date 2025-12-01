package aiagents.bazar.api.service;

import aiagents.bazar.api.dto.task.TaskImageResponseDto;
import aiagents.bazar.api.exeption.NotFoundTaskException;
import aiagents.bazar.data.entity.Task;
import aiagents.bazar.data.entity.TaskImage;
import aiagents.bazar.data.repository.TaskImageRepository;
import aiagents.bazar.data.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TaskImageService {

    private final TaskImageRepository taskImageRepository;
    private final TaskRepository taskRepository;
    private final StorageService storageService;

    @Transactional
    public List<TaskImageResponseDto> uploadImages(Long taskId, MultipartFile[] files) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundTaskException("Task not found with id: " + taskId));

        List<TaskImage> images = IntStream.range(0, files.length)
                .mapToObj(i -> {
                    MultipartFile file = files[i];
                    String s3Key = storageService.uploadImage(file, taskId);
                    String url = storageService.getPublicUrl(s3Key);

                    TaskImage taskImage = new TaskImage();
                    taskImage.setTask(task);
                    taskImage.setS3Key(s3Key);
                    taskImage.setUrl(url);
                    taskImage.setSortOrder(i);

                    return taskImage;
                })
                .toList();

        List<TaskImage> savedImages = taskImageRepository.saveAll(images);
        return savedImages.stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional
    public List<TaskImageResponseDto> getImagesByTaskId(Long taskId) {
        List<TaskImage> images = taskImageRepository.findByTaskIdOrderBySortOrderAsc(taskId);
        return images.stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional
    public void deleteImage(Long imageId) {
        TaskImage image = taskImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + imageId));

        // Delete from MinIO
        storageService.deleteImage(image.getS3Key());

        // Delete from database
        taskImageRepository.delete(image);
    }

    private TaskImageResponseDto toResponseDto(TaskImage image) {
        return TaskImageResponseDto.builder()
                .id(image.getId())
                .url(image.getUrl())
                .sortOrder(image.getSortOrder())
                .build();
    }
}

