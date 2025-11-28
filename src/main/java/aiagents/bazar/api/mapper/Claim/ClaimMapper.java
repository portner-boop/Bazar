package aiagents.bazar.api.mapper.Claim;

import aiagents.bazar.api.dto.claim.ClaimCreateDto;
import aiagents.bazar.api.dto.claim.ClaimResponseDto;
import aiagents.bazar.api.dto.claim.ClaimUpdateDto;
import aiagents.bazar.data.entity.Claim;
import aiagents.bazar.data.entity.ClaimStatus;
import aiagents.bazar.data.entity.Task;
import aiagents.bazar.data.entity.TelegramUser;
import org.springframework.stereotype.Component;

@Component
public class ClaimMapper {

    public ClaimResponseDto toResponseDto(Claim claim) {
        if (claim == null) return null;

        ClaimResponseDto dto = new ClaimResponseDto();
        dto.setId(claim.getId());
        dto.setStatus(claim.getStatus());
        dto.setMessage(claim.getMessage());
        dto.setCreatedAt(claim.getCreatedAt());

        if (claim.getTask() != null) dto.setTaskId(claim.getTask().getId());
        if (claim.getTelegramUser() != null) dto.setTelegramUserId(claim.getTelegramUser().getId());

        return dto;
    }

    public void updateFromDto(ClaimUpdateDto dto, Claim claim) {
        if (dto == null || claim == null) return;

        if (dto.getStatus() != null) claim.setStatus(dto.getStatus());
        if (dto.getMessage() != null) claim.setMessage(dto.getMessage());
    }

    public Claim fromCreateDto(ClaimCreateDto dto, Task task, TelegramUser user) {
        if (dto == null || task == null || user == null) {
            throw new IllegalArgumentException("ClaimCreateDto, task and user must be non-null");
        }

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.PENDING);
        claim.setMessage(dto.getMessage());
        claim.setTask(task);
        claim.setTelegramUser(user);
        return claim;
    }
}
