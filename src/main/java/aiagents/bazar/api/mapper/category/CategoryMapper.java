package aiagents.bazar.api.mapper.category;

import aiagents.bazar.api.dto.category.CategoryResponseDto;
import aiagents.bazar.api.dto.category.CategoryUpdateDto;
import aiagents.bazar.data.entity.Category;
import org.mapstruct.*;
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
}
