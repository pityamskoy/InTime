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
    private final ImageRepository imageRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ShopRepository shopRepository;

    public CategoryService(
            CategoryRepository categoryRepository,
            ImageRepository imageRepository,
            ProductTypeRepository productTypeRepository,
            ShopRepository shopRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.productTypeRepository = productTypeRepository;
        this.shopRepository = shopRepository;
    }

    public Category createCategory(Category categoryToCreate) {
        Category newCategory = new Category(
                categoryToCreate.getId(),
                categoryToCreate.getName(),
                categoryToCreate.getDescription(),
                categoryToCreate.getProductTypes()
        );

        for(ProductType prt:newCategory.getProductTypes()){
            for(Image img:prt.getImages())
                imageRepository.save(img);
            for(Image img:prt.getShop().getImages())
                imageRepository.save(img);
            shopRepository.save(prt.getShop());
            productTypeRepository.save(prt);
        }

        return categoryRepository.save(newCategory);
    }

    public List<Category> getAllCategorys() {
        return categoryRepository.findAll();
    }
}