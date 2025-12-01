package aiagents.bazar.api.controller;

import aiagents.bazar.api.dto.PageResponseDto;
import aiagents.bazar.api.dto.claim.ClaimCreateDto;
import aiagents.bazar.api.dto.claim.ClaimResponseDto;
import aiagents.bazar.api.dto.claim.ClaimUpdateDto;
import aiagents.bazar.api.service.ClaimService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/claims")
@RequiredArgsConstructor
@Validated
public class ClaimController {

    private final ClaimService service;

    @PostMapping
    public ResponseEntity<ClaimResponseDto> create(@Valid @RequestBody ClaimCreateDto dto) {
        ClaimResponseDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/v1/claims/" + created.getId()))
                .body(created);
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<ClaimResponseDto>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long taskId,
            @RequestParam(required = false) Long telegramUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClaimResponseDto> result = service.getAll(status, taskId, telegramUserId, pageable);
        PageResponseDto<ClaimResponseDto> response = PageResponseDto.<ClaimResponseDto>builder()
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
    public ResponseEntity<ClaimResponseDto> getById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClaimResponseDto> update(@PathVariable @Positive Long id,
                                                   @Valid @RequestBody ClaimUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
