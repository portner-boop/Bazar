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

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Transactional
    public CategoryResponseDto create(CategoryCreateDto dto) {
        Category category = mapper.fromCreateDto(dto);
        Category saved = repository.save(category);
        return mapper.toResponseDTO(saved);
    }

    @Transactional
    public List<CategoryResponseDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public CategoryResponseDto getById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponseDTO)
                .orElseThrow(() -> new NotFoundCategoryException("Category not found with id: " + id));
    }

    @Transactional
    public CategoryResponseDto update(Long id, CategoryUpdateDto dto) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new NotFoundCategoryException("Category not found with id: " + id));
        mapper.updateFromDto(dto, category);
        Category updated = repository.save(category);
        return mapper.toResponseDTO(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundCategoryException("Category not found with id: " + id);
        }
        repository.deleteById(id);
    }
}

