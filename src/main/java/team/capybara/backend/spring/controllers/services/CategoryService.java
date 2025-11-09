package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.category.CategoryDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.CategoryMapper;
import team.capybara.backend.spring.controllers.repositories.*;
import team.capybara.backend.spring.entities.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public final class CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    public CategoryService(
            CategoryMapper categoryMapper,
            CategoryRepository categoryRepository
    ) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(categoryMapper::getEntity).toList();
    }

    public Optional<CategoryDto> getCategoryById(UUID id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (categoryOptional.isPresent()) {
            CategoryDto categoryDto = categoryMapper.getEntity(categoryOptional.get());
            return Optional.of(categoryDto);
        }

        return Optional.empty();
    }

    public CategoryDto createCategory(CategoryDto categoryToCreate) {
        try {
            return categoryMapper.postEntity(categoryToCreate);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public CategoryDto updateCategory(CategoryDto categoryToUpdate) {
        try {
            return categoryMapper.putEntity(categoryToUpdate);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public void deleteCategory(UUID id) {
        try {
            categoryRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}