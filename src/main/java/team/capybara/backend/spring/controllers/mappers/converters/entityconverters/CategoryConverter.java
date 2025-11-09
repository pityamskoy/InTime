package team.capybara.backend.spring.controllers.mappers.converters.entityconverters;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mappers.converters.EntityIdConverter;
import team.capybara.backend.spring.controllers.repositories.CategoryRepository;
import team.capybara.backend.spring.entities.Category;

import java.util.Optional;
import java.util.UUID;

@Component
public final class CategoryConverter implements EntityIdConverter<Category> {
    private final CategoryRepository categoryRepository;

    public CategoryConverter(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category toEntity(UUID id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (categoryOptional.isEmpty()) {
            throw new EntityNotFoundException("Category not found; id=" + id);
        }

        return categoryOptional.get();
    }
}
