package team.capybara.backend.spring.controllers.services;

import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.repositories.*;
import team.capybara.backend.spring.entities.Category;
import team.capybara.backend.spring.entities.Image;
import team.capybara.backend.spring.entities.ProductType;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(
            CategoryRepository categoryRepository
    ) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category categoryToCreate) {
        Category newCategory = new Category(
                categoryToCreate.getId(),
                categoryToCreate.getName(),
                categoryToCreate.getDescription(),
                categoryToCreate.getProductTypes()
        );

        return categoryRepository.save(newCategory);
    }

    public List<Category> getAllCategorys() {
        return categoryRepository.findAll();
    }
}