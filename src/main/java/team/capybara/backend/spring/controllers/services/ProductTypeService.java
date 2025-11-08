package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.producttype.ProductTypeDto;
import team.capybara.backend.spring.controllers.mapping.entitymappers.ProductTypeMapper;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;
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

    public ProductTypeService(
            ProductTypeMapper productTypeMapper,
            ProductTypeRepository productTypeRepository,
            ShopRepository shopRepository
    ) {
        this.productTypeMapper = productTypeMapper;
        this.productTypeRepository = productTypeRepository;
        this.shopRepository = shopRepository;
    }

    public List<ProductTypeDto> getAllProductTypes() {
        List<ProductType> productTypes = productTypeRepository.findAll();

        return productTypes.stream().map(productTypeMapper::getEntity).toList();
    }

    //fix soon. Don't throw exceptions. Use Optional
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

    public ProductTypeDto createProductType(ProductTypeDto productTypeToCreate) {
        try {
            return productTypeMapper.postEntity(productTypeToCreate);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public ProductTypeDto updateProductType(ProductTypeDto productTypeToUpdate) {
        try {
            return productTypeMapper.putEntity(productTypeToUpdate);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteProductType(UUID id) {
        try {
            productTypeMapper.deleteEntity(id);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}