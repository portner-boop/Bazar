package aiagents.bazar.api.mapper.category;

import aiagents.bazar.api.dto.category.CategoryCreateDto;
import aiagents.bazar.api.dto.category.CategoryResponseDto;
import aiagents.bazar.api.dto.category.CategoryUpdateDto;
import aiagents.bazar.data.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponseDto toResponseDTO(Category category) {
        if (category == null) return null;

        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }

    public void updateFromDto(CategoryUpdateDto dto, Category category) {
        if (dto == null || category == null) return;

        if (dto.getName() != null) category.setName(dto.getName());
        if (dto.getDescription() != null) category.setDescription(dto.getDescription());
    }

    public Category fromCreateDto(CategoryCreateDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("CategoryCreateDto must be non-null");
        }
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }
}
