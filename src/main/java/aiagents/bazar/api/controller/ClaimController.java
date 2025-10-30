package aiagents.bazar.api.controller;

import aiagents.bazar.api.dto.claim.ClaimCreateDto;
import aiagents.bazar.api.dto.claim.ClaimResponseDto;
import aiagents.bazar.api.dto.claim.ClaimUpdateDto;
import aiagents.bazar.api.service.ClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/claims")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService service;

    @PostMapping
    public Mono<ClaimResponseDto> create(@RequestBody ClaimCreateDto dto) {
        return service.create(dto);
    }

    @GetMapping
    public Flux<ClaimResponseDto> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long taskId,
            @RequestParam(required = false) Long telegramUserId
    ) {
        return service.getAll(status, taskId, telegramUserId);
    }

    @GetMapping("/{id}")
    public Mono<ClaimResponseDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Mono<ClaimResponseDto> update(@PathVariable Long id, @RequestBody ClaimUpdateDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
