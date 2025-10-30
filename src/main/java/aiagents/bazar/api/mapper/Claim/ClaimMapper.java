package aiagents.bazar.api.mapper.Claim;

import aiagents.bazar.api.dto.claim.ClaimResponseDto;
import aiagents.bazar.api.dto.claim.ClaimUpdateDto;
import aiagents.bazar.data.entity.Claim;
import org.mapstruct.*;
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
}
