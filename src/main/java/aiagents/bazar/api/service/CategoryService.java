package aiagents.bazar.api.service;

import aiagents.bazar.api.dto.category.CategoryCreateDto;
import aiagents.bazar.api.dto.category.CategoryResponseDto;
import aiagents.bazar.api.dto.category.CategoryUpdateDto;
import aiagents.bazar.api.exeption.NotFoundCategoryException;
import aiagents.bazar.api.mapper.category.CategoryMapper;
import aiagents.bazar.data.entity.Category;
import aiagents.bazar.data.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Transactional
    public Mono<CategoryResponseDto> create(CategoryCreateDto dto) {
        return Mono.fromCallable(() -> {
            Category category = new Category();
            category.setName(dto.getName());
            category.setDescription(dto.getDescription());
            Category saved = repository.save(category);
            return mapper.toResponseDTO(saved);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Flux<CategoryResponseDto> getAll() {
        List<Category> categories = repository.findAll();
        return Flux.fromIterable(categories)
                .map(mapper::toResponseDTO);
    }

    @Transactional
    public Mono<CategoryResponseDto> getById(Long id) {
        return Mono.fromCallable(() -> repository.findById(id)
                .map(mapper::toResponseDTO)
                .orElseThrow(() -> new NotFoundCategoryException("Category not found with id: " + id))
        ).subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<CategoryResponseDto> update(Long id, CategoryUpdateDto dto) {
        return Mono.fromCallable(() -> {
            Category category = repository.findById(id)
                    .orElseThrow(() -> new NotFoundCategoryException("Category not found with id: " + id));
            mapper.updateFromDto(dto, category);
            Category updated = repository.save(category);
            return mapper.toResponseDTO(updated);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<Void> delete(Long id) {
        return Mono.fromRunnable(() -> {
            if (!repository.existsById(id)) {
                throw new NotFoundCategoryException("Category not found with id: " + id);
            }
            repository.deleteById(id);
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
}

