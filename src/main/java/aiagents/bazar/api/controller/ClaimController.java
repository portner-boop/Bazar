package aiagents.bazar.api.controller;

import aiagents.bazar.api.dto.claim.ClaimCreateDto;
import aiagents.bazar.api.dto.claim.ClaimResponseDto;
import aiagents.bazar.api.dto.claim.ClaimUpdateDto;
import aiagents.bazar.api.service.ClaimService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
    public ResponseEntity<List<ClaimResponseDto>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long taskId,
            @RequestParam(required = false) Long telegramUserId
    ) {
        return ResponseEntity.ok(service.getAll(status, taskId, telegramUserId));
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
