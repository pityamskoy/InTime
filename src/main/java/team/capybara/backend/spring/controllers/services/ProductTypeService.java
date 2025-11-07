package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
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
public final class ProductTypeService {
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

    public ProductType updateProductType(@RequestBody ProductTypeDto productTypeDto) {
        try {
            return productTypeMapper.putEntity(productTypeDto);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteProductType(UUID id) {
        try {
            productTypeRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}