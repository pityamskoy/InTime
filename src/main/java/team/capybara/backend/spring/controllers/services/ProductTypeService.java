package team.capybara.backend.spring.controllers.services;

import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.repositories.ImageRepository;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;
import team.capybara.backend.spring.entities.Image;
import team.capybara.backend.spring.entities.ProductType;
import team.capybara.backend.spring.entities.Shop;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductTypeService {
    private final ProductTypeRepository productTypeRepository;
    private final ShopRepository shopRepository;
    private final ImageRepository imageRepository;

    public ProductTypeService(
            ProductTypeRepository productTypeRepository,
            ShopRepository shopRepository,
            ImageRepository imageRepository
    ) {
        this.productTypeRepository = productTypeRepository;
        this.shopRepository = shopRepository;
        this.imageRepository = imageRepository;
    }

    //fix through dto
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

    public List<ProductType> getAllProductTypesByShopId(String shopId) {
        Optional<Shop> shop = shopRepository.findById(UUID.fromString(shopId));

        if (shop.isPresent()) {
            //List<ProductType> = shop.get().get
            return null;
        } else {
            throw new ServiceException("Shop not found by id: " + shopId);
        }
    }

    public Optional<ProductType> getProductTypeById(String id) {
        return Optional.empty();
    }
}