package team.capybara.backend.spring.controllers.services;

import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.producttype.ProductTypeDto;
import team.capybara.backend.spring.controllers.mapping.entitymappers.ProductTypeMapper;
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
    private final ProductTypeMapper productTypeMapper;
    private final ProductTypeRepository productTypeRepository;
    private final ShopRepository shopRepository;
    private final ImageRepository imageRepository;

    public ProductTypeService(
            ProductTypeMapper productTypeMapper,
            ProductTypeRepository productTypeRepository,
            ShopRepository shopRepository,
            ImageRepository imageRepository
    ) {
        this.productTypeMapper = productTypeMapper;
        this.productTypeRepository = productTypeRepository;
        this.shopRepository = shopRepository;
        this.imageRepository = imageRepository;
    }

    public ProductType createProductType(ProductTypeDto productTypeDto) {
        ProductType productType = productTypeMapper.postEntity(productTypeDto);

        for(Image img:productType.getImages())
            imageRepository.save(img);

        for(Image img:productType.getShop().getImages())
            imageRepository.save(img);

        shopRepository.save(productType.getShop());

        return productTypeRepository.save(productType);
    }

    public List<ProductTypeDto> getAllProductTypes() {
        List<ProductType> productTypes = productTypeRepository.findAll();

        return productTypes.stream().map(productTypeMapper::getEntity).toList();
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

    public Optional<ProductType> getProductTypeById(UUID id) {
        return productTypeRepository.findById(id);
    }
}