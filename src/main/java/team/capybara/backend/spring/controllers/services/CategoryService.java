package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.category.CategoryDto;
import team.capybara.backend.spring.controllers.dto.category.CategoryWithIdDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.CategoryMapper;
import team.capybara.backend.spring.controllers.repositories.*;
import team.capybara.backend.spring.entities.Category;
import team.capybara.backend.spring.entities.ProductType;

import java.util.ArrayList;
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

    public List<CategoryWithIdDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(categoryMapper::getEntity).toList();
    }

    public Optional<CategoryWithIdDto> getCategoryById(UUID id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (categoryOptional.isPresent()) {
            CategoryWithIdDto categoryWithIdDto = categoryMapper.getEntity(categoryOptional.get());
            return Optional.of(categoryWithIdDto);
        }

        return Optional.empty();
    }

    public List<CategoryWithIdDto> getAllCategoriesByProductTypeId(UUID productTypeId) {
        List<Category> allCategories = categoryRepository.findAll();
        List<CategoryWithIdDto> categories = new ArrayList<>();

        for (Category category : allCategories) {
            List <ProductType> productTypes = category.getProductTypes();
            for (ProductType productType : productTypes) {
                if (productType.getId().equals(productTypeId)) {
                    categories.add(categoryMapper.getEntity(category));
                }
            }
        }

        return categories;
    }

    public CategoryWithIdDto createCategory(CategoryDto categoryToCreate) {
        try {
            return categoryMapper.postEntity(categoryToCreate);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public CategoryWithIdDto updateCategory(CategoryWithIdDto categoryToUpdate) {
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