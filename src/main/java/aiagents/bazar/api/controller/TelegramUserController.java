package aiagents.bazar.api.controller;

import aiagents.bazar.api.dto.PageResponseDto;
import aiagents.bazar.api.dto.telegramuser.TelegramUserResponseDto;
import aiagents.bazar.api.dto.telegramuser.TelegramUserUpdateDto;
import aiagents.bazar.api.dto.telegramuser.UserRoleUpdateDto;
import aiagents.bazar.api.service.TelegramUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class TelegramUserController {

    private final TelegramUserService service;

    @GetMapping
    public ResponseEntity<PageResponseDto<TelegramUserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TelegramUserResponseDto> result = service.getAllUsers(pageable);
        PageResponseDto<TelegramUserResponseDto> response = PageResponseDto.<TelegramUserResponseDto>builder()
                .content(result.getContent())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .first(result.isFirst())
                .last(result.isLast())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TelegramUserResponseDto> getUserById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TelegramUserResponseDto> updateUser(
            @PathVariable @Positive Long id,
            @Valid @RequestBody TelegramUserUpdateDto updateDto
    ) {
        return ResponseEntity.ok(service.updateUser(id, updateDto));
    }

    @GetMapping("/telegram/{telegramUserId}")
    public ResponseEntity<TelegramUserResponseDto> getUserByTelegramId(
            @PathVariable @Positive Long telegramUserId){
        return ResponseEntity.ok(service.getUserByTelegramId(telegramUserId));
    }

    @PutMapping("/{targetUserId}/role")
    public ResponseEntity<TelegramUserResponseDto> updateUserRole(
            @PathVariable @Positive Long targetUserId,
            @RequestParam @Positive Long adminId,
            @Valid @RequestBody UserRoleUpdateDto dto
    ) {
        return ResponseEntity.ok(service.updateUserRole(adminId, targetUserId, dto));
    }
}