package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.entities.producttype.ProductTypeDto;
import team.capybara.backend.spring.controllers.dto.entities.producttype.ProductTypeWithIdDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.ProductTypeMapper;
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

    public List<ProductTypeWithIdDto> getAllProductTypes() {
        List<ProductType> productTypes = productTypeRepository.findAll();

        return productTypes.stream().map(productTypeMapper::getEntity).toList();
    }

    public Optional<List<ProductTypeWithIdDto>> getAllProductTypesByShopId(UUID shopId) {
        Optional<Shop> shop = shopRepository.findById(shopId);
        List<ProductType> productTypes = productTypeRepository.findByShop(shop.get());

        return Optional.of(productTypes.stream().map(productTypeMapper::getEntity).toList());
    }

    public Optional<ProductTypeWithIdDto> getProductTypeById(UUID id) {
        Optional<ProductType> productTypeOptional = productTypeRepository.findById(id);

        if (productTypeOptional.isPresent()) {
            ProductTypeWithIdDto productTypeWithIdDto = productTypeMapper.getEntity(productTypeOptional.get());
            return Optional.of(productTypeWithIdDto);
        }

        return Optional.empty();
    }

    public ProductTypeWithIdDto createProductType(ProductTypeDto productTypeToCreate) {
        try {
            return productTypeMapper.postEntity(productTypeToCreate);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public ProductTypeWithIdDto updateProductType(ProductTypeWithIdDto productTypeToUpdate) {
        try {
            return productTypeMapper.putEntity(productTypeToUpdate);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public void deleteProductType(UUID id) {
        try {
            productTypeMapper.deleteEntity(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}