package team.capybara.backend.spring.controllers.mappers.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.dto.category.CategoryDto;
import team.capybara.backend.spring.controllers.dto.category.CategoryWithIdDto;
import team.capybara.backend.spring.controllers.mappers.Mapper;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.CategoryConverter;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ProductTypeConverter;
import team.capybara.backend.spring.controllers.repositories.CategoryRepository;
import team.capybara.backend.spring.entities.Category;
import team.capybara.backend.spring.entities.ProductType;

import java.util.List;
import java.util.UUID;

@Component
public final class CategoryMapper implements Mapper<Category, CategoryWithIdDto, CategoryDto> {
    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;
    private final ProductTypeConverter productTypeConverter;

    public CategoryMapper(
            CategoryRepository categoryRepository,
            CategoryConverter categoryConverter,
            ProductTypeConverter productTypeConverter
    ) {
        this.categoryRepository = categoryRepository;
        this.categoryConverter = categoryConverter;
        this.productTypeConverter = productTypeConverter;
    }

    @Override
    public CategoryWithIdDto getEntity(Category category) {
        return new CategoryWithIdDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                productTypeConverter.toIdList(category.getProductTypes())
        );
    }

    @Override
    public CategoryWithIdDto postEntity(CategoryDto categoryToCreate) {
        try {
            List<ProductType> productTypes = productTypeConverter.toEntityList(categoryToCreate.productTypesId());

            Category categoryCreated = categoryRepository.save(new Category(
                    UUID.randomUUID(),
                    categoryToCreate.name(),
                    categoryToCreate.description(),
                    productTypes
            ));

            return getEntity(categoryCreated);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public CategoryWithIdDto putEntity(CategoryWithIdDto categoryToUpdate) {
        try {
            Category categoryUpdated = categoryConverter.toEntity(categoryToUpdate.id());
            List<ProductType> productTypes = productTypeConverter.toEntityList(categoryToUpdate.productTypesId());

            categoryUpdated.setName(categoryToUpdate.name());
            categoryUpdated.setDescription(categoryToUpdate.description());
            categoryUpdated.setProductTypes(productTypes);
            categoryRepository.save(categoryUpdated);

            return getEntity(categoryUpdated);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public void deleteEntity(UUID entityId) {
        try {
            Category category = categoryConverter.toEntity(entityId);
            categoryRepository.delete(category);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
