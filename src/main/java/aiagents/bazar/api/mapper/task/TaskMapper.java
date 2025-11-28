package aiagents.bazar.api.mapper.task;

import aiagents.bazar.api.dto.task.TaskCreateDto;
import aiagents.bazar.api.dto.task.TaskResponseDto;
import aiagents.bazar.api.dto.task.TaskUpdateDto;
import aiagents.bazar.data.entity.*;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponseDto toResponseDTO(Task task) {
        if (task == null) return null;

        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setRegion(task.getRegion());
        dto.setPriceExpected(task.getPriceExpected());
        dto.setRewardAmount(task.getRewardAmount());
        dto.setRewardPercentage(task.getRewardPercentage());
        dto.setRewardType(task.getRewardType());
        dto.setStatus(task.getStatus());
        dto.setEscrowStatus(task.getEscrowStatus());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());

        if (task.getCategory() != null) dto.setCategoryId(task.getCategory().getId());
        if (task.getTelegramUser() != null) dto.setTelegramUserId(task.getTelegramUser().getId());

        return dto;
    }

    public void updateFromDto(TaskUpdateDto dto, Task task) {
        if (dto == null || task == null) return;

        if (dto.getTitle() != null) task.setTitle(dto.getTitle());
        if (dto.getDescription() != null) task.setDescription(dto.getDescription());
        if (dto.getRegion() != null) task.setRegion(dto.getRegion());
        if (dto.getPriceExpected() != null) task.setPriceExpected(dto.getPriceExpected());
        if (dto.getRewardAmount() != null) task.setRewardAmount(dto.getRewardAmount());
        if (dto.getRewardPercentage() != null) task.setRewardPercentage(dto.getRewardPercentage());
        if (dto.getRewardType() != null) task.setRewardType(dto.getRewardType());
        if (dto.getStatus() != null) task.setStatus(dto.getStatus());
        if (dto.getEscrowStatus() != null) task.setEscrowStatus(dto.getEscrowStatus());
    }

    public Task fromCreateDto(TaskCreateDto dto, Category category, TelegramUser creator) {
        if (dto == null || category == null || creator == null) {
            throw new IllegalArgumentException("TaskCreateDto, category and creator must be non-null");
        }

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setRegion(dto.getRegion());
        task.setPriceExpected(dto.getPriceExpected());
        task.setRewardAmount(dto.getRewardAmount());
        task.setRewardPercentage(dto.getRewardPercentage());
        task.setRewardType(dto.getRewardType() != null ? dto.getRewardType() : RewardType.FIXED_AMOUNT);
        task.setStatus(dto.getStatus() != null ? dto.getStatus() : TaskStatus.NEW);
        task.setEscrowStatus(dto.getEscrowStatus() != null ? dto.getEscrowStatus() : EscrowStatus.NOT_REQUIRED);
        task.setCategory(category);
        task.setTelegramUser(creator);
        return task;
    }
}
