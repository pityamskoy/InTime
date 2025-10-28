package team.capybara.backend.spring.controllers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.repositories.ImageRepository;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;
import team.capybara.backend.spring.entitys.Image;
import team.capybara.backend.spring.entitys.ProductType;

import java.util.List;

@Service
public class ProductTypeService {
    private final ProductTypeRepository productTypeRepository;
    private final ShopRepository shopRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public ProductTypeService(ProductTypeRepository productTypeRepository, ShopRepository shopRepository, ImageRepository imageRepository) {
        this.productTypeRepository = productTypeRepository;
        this.shopRepository = shopRepository;
        this.imageRepository = imageRepository;
    }

    public ProductType createProductType(ProductType productTypeToCreate) {
        ProductType newProductType = new ProductType(
                productTypeToCreate.getId(),
                productTypeToCreate.getName(),
                productTypeToCreate.getDescription(),
                productTypeToCreate.getMainImagePath(),
                productTypeToCreate.getImages(),
                productTypeToCreate.getShop()
        );

        for(Image img:newProductType.getImages())
            imageRepository.save(img);

        for(Image img:newProductType.getShop().getImages())
            imageRepository.save(img);

        shopRepository.save(newProductType.getShop());

        return productTypeRepository.save(newProductType);
    }

    public List<ProductType> getAllProductTypes() {
        return productTypeRepository.findAll();
    }
}