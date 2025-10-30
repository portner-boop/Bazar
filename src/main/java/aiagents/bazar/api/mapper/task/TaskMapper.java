package aiagents.bazar.api.mapper.task;

import aiagents.bazar.api.dto.task.TaskResponseDto;
import aiagents.bazar.api.dto.task.TaskUpdateDto;
import aiagents.bazar.data.entity.Task;
import org.mapstruct.*;
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
}
