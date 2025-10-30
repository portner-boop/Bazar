package aiagents.bazar.api.controller;

import aiagents.bazar.api.dto.category.CategoryCreateDto;
import aiagents.bazar.api.dto.category.CategoryResponseDto;
import aiagents.bazar.api.dto.category.CategoryUpdateDto;
import aiagents.bazar.api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public Mono<CategoryResponseDto> create(@RequestBody CategoryCreateDto dto) {
        return service.create(dto);
    }

    @GetMapping
    public Flux<CategoryResponseDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Mono<CategoryResponseDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Mono<CategoryResponseDto> update(@PathVariable Long id, @RequestBody CategoryUpdateDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }
}