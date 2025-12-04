package team.capybara.backend.spring.controllers.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.entities.category.CategoryDto;
import team.capybara.backend.spring.controllers.dto.entities.category.CategoryWithIdDto;
import team.capybara.backend.spring.controllers.services.CategoryService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/categories")
@SuppressWarnings(value = {"unused"})
public final class CategoryController {
    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping
    public ResponseEntity<List<CategoryWithIdDto>> getAllCategories() {
        log.info("Called getAllCategories");
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryWithIdDto> getCategoryById(@PathVariable String id) {
        log.info("Called getCategoryById; id={}", id);
        Optional<CategoryWithIdDto> categoryWithIdDtoOptional = categoryService.getCategoryById(UUID.fromString(id));

        return categoryWithIdDtoOptional.map(ResponseEntity::ok).orElseGet
                (() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryWithIdDto> createCategory(@RequestBody CategoryDto categoryToCreate) {
        log.info("Called createCategory; categoryToCreate={}", categoryToCreate);

        try {
            return ResponseEntity.status(HttpStatus.CREATED).
                    body(categoryService.createCategory(categoryToCreate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<CategoryWithIdDto> updateCategory(@RequestBody CategoryWithIdDto categoryToUpdate) {
        log.info("Called updateCategory; categoryToUpdate={}", categoryToUpdate);

        try {
            return ResponseEntity.ok(categoryService.updateCategory(categoryToUpdate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        log.info("Called deleteCategory; id={}", id);

        try {
            categoryService.deleteCategory(UUID.fromString(id));
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
