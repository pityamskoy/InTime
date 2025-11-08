package team.capybara.backend.spring.controllers.mappers.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mappers.Mapper;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ImageConverter;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ProductTypeConverter;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ShopConverter;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;
import team.capybara.backend.spring.entities.Image;
import team.capybara.backend.spring.entities.ProductType;
import team.capybara.backend.spring.entities.Shop;
import team.capybara.backend.spring.controllers.dto.producttype.ProductTypeDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public final class ProductTypeMapper implements Mapper<ProductType, ProductTypeDto> {
    private final ProductTypeRepository productTypeRepository;
    private final ProductTypeConverter productTypeConverter;
    private final ImageConverter imageConverter;
    private final ShopConverter shopConverter;

    public ProductTypeMapper(
            ProductTypeRepository productTypeRepository,
            ProductTypeConverter productTypeConverter,
            ImageConverter imageConverter,
            ShopConverter shopConverter
    ) {
        this.productTypeRepository = productTypeRepository;
        this.productTypeConverter = productTypeConverter;
        this.imageConverter = imageConverter;
        this.shopConverter = shopConverter;
    }

    @Override
    public ProductTypeDto getEntity(ProductType productType) {
        List<UUID> imagesId = imageConverter.toIdList(productType.getImages());

        return new ProductTypeDto(
                productType.getId(),
                productType.getName(),
                productType.getDescription(),
                productType.getMainImagePath(),
                imagesId,
                productType.getShop().getId()
        );
    }

    @Override
    public ProductTypeDto postEntity(ProductTypeDto productTypeToCreate) {
        try {
            Shop shop = shopConverter.toEntity(productTypeToCreate.shopId());
            List<Image> images = imageConverter.toEntityList(productTypeToCreate.imagesId());

            ProductType productTypeCreated = new ProductType(
                    UUID.randomUUID(),
                    productTypeToCreate.name(),
                    productTypeToCreate.description(),
                    productTypeToCreate.mainImagePath(),
                    images,
                    shop
            );

            return getEntity(productTypeCreated);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public ProductTypeDto putEntity(ProductTypeDto productTypeToUpdate) {
        try {
            ProductType productTypeUpdated = productTypeConverter.toEntity(productTypeToUpdate.id());
            Shop shop = shopConverter.toEntity(productTypeToUpdate.shopId());
            List<Image> images = imageConverter.toEntityList(productTypeToUpdate.imagesId());

            productTypeUpdated.setName(productTypeToUpdate.name());
            productTypeUpdated.setDescription(productTypeToUpdate.description());
            productTypeUpdated.setMainImagePath(productTypeToUpdate.mainImagePath());
            productTypeUpdated.setImages(images);
            productTypeUpdated.setShop(shop);
            productTypeRepository.save(productTypeUpdated);

            return getEntity(productTypeUpdated);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public void deleteEntity(UUID id) {
        Optional<ProductType> productType = productTypeRepository.findById(id);

        if (productType.isEmpty()) {
            throw new EntityNotFoundException("Not found product type; id=" + id);
        }

        productTypeRepository.deleteById(id);
    }
}
