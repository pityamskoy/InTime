package team.capybara.backend.spring.controllers.mapping.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mapping.Mapper;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;
import team.capybara.backend.spring.entities.Image;
import team.capybara.backend.spring.entities.ProductType;
import team.capybara.backend.spring.entities.Shop;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;
import team.capybara.backend.spring.controllers.dto.producttype.ProductTypeDto;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProductTypeMapper implements Mapper<ProductType, ProductTypeDto> {
    private final ShopRepository shopRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ImageMapper imageMapper;


    public ProductTypeMapper(
            ShopRepository shopRepository,
            ProductTypeRepository productTypeRepository,
            ImageMapper imageMapper) {
        this.shopRepository = shopRepository;
        this.productTypeRepository = productTypeRepository;
        this.imageMapper = imageMapper;
    }


    @Override
    public ProductTypeDto getEntity(ProductType productType) {
        List<ImageDto> images = imageMapper.toDtoList(productType.getImages());

        return new ProductTypeDto(
                productType.getId(),
                productType.getName(),
                productType.getDescription(),
                productType.getMainImagePath(),
                images,
                productType.getShop().getId()
        );
    }

    @Override
    public ProductType postEntity(ProductTypeDto productTypeDto) {
        UUID shopId = productTypeDto.shopId();
        List<Image> images = imageMapper.toEntityList(productTypeDto.imagesDto());
        Optional<Shop> optionalShop = this.shopRepository.findById(shopId);

        if (optionalShop.isEmpty()) {
            throw new EntityNotFoundException("Not found shop with id: " + shopId);
        }

        return new ProductType(
                UUID.randomUUID(),
                productTypeDto.name(),
                productTypeDto.description(),
                productTypeDto.mainImagePath(),
                images,
                optionalShop.get()
        );
    }

    @Override
    public void putEntity(ProductTypeDto dtoObjectWithId) {
        Optional<ProductType> productType = productTypeRepository.findById(dtoObjectWithId.id());
        if (productType.isEmpty()) {
            throw new EntityNotFoundException("Not found product type with id: " + dtoObjectWithId.id());
        }
        Optional<Shop> shop =  shopRepository.findById(dtoObjectWithId.shopId());
        if (shop.isEmpty()) {
            throw new EntityNotFoundException("Not found shop with id: " + dtoObjectWithId.shopId());
        }

        ProductType obj = productType.get();
        obj.setName(dtoObjectWithId.name());
        obj.setDescription(dtoObjectWithId.description());
        obj.setMainImagePath(dtoObjectWithId.mainImagePath());
        obj.setShop(shop.get());

        List<Image> images = imageMapper.toEntityList(dtoObjectWithId.imagesDto());
        obj.setImages(images);
    }


    @Override
    public void removeEntity(UUID entityId) {
        Optional<ProductType> productType = productTypeRepository.findById(entityId);
        if (productType.isEmpty()) {
            throw new EntityNotFoundException("Not found product type with id: " + entityId);
        }
        productTypeRepository.deleteById(entityId);
    }
}
