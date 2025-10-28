package team.capybara.backend.spring.controllers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.repositories.*;
import team.capybara.backend.spring.entitys.Category;
import team.capybara.backend.spring.entitys.Image;
import team.capybara.backend.spring.entitys.ProductType;
import team.capybara.backend.spring.entitys.Shop;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ImageRepository imageRepository, ProductTypeRepository productTypeRepository, ShopRepository shopRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.productTypeRepository = productTypeRepository;
        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
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