package aiagents.bazar.api.controller;

import aiagents.bazar.api.dto.TelegramUserResponseDto;
import aiagents.bazar.api.dto.TelegramUserUpdateDto;
import aiagents.bazar.api.service.TelegramUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class TelegramUserController {

    private final TelegramUserService service;

    @GetMapping
    public Flux<TelegramUserResponseDto> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public Mono<TelegramUserResponseDto> getUserById(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @PutMapping("/{id}")
    public Mono<TelegramUserResponseDto> updateUser(
            @PathVariable Long id,
            @RequestBody TelegramUserUpdateDto updateDto
    ) {
        return service.updateUser(id, updateDto);
    }
}