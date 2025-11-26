package team.capybara.backend.spring.controllers.mappers.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.dto.entities.producttype.ProductTypeDto;
import team.capybara.backend.spring.controllers.mappers.Mapper;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ImageConverter;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ProductTypeConverter;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ShopConverter;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;
import team.capybara.backend.spring.entities.Image;
import team.capybara.backend.spring.entities.ProductType;
import team.capybara.backend.spring.entities.Shop;
import team.capybara.backend.spring.controllers.dto.entities.producttype.ProductTypeWithIdDto;

import java.util.List;
import java.util.UUID;

@Component
public final class ProductTypeMapper implements Mapper<ProductType, ProductTypeWithIdDto, ProductTypeDto> {
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
    public ProductTypeWithIdDto getEntity(ProductType productType) {
        List<UUID> imagesId = imageConverter.toIdList(productType.getImages());

        return new ProductTypeWithIdDto(
                productType.getId(),
                productType.getName(),
                productType.getDescription(),
                productType.getWeight(),
                productType.getCalories(),
                productType.getProtein(),
                productType.getFats(),
                productType.getCarbohydrates(),
                productType.getQuantityInOnePackage(),
                productType.getMainImagePath(),
                imagesId,
                productType.getShop().getId()
        );
    }

    @Override
    public ProductTypeWithIdDto postEntity(ProductTypeDto productTypeToCreate) {
        try {
            Shop shop = shopConverter.toEntity(productTypeToCreate.shopId());
            List<Image> images = imageConverter.toEntityList(productTypeToCreate.imagesId());

            ProductType productTypeCreated = new ProductType(
                    UUID.randomUUID(),
                    productTypeToCreate.name(),
                    productTypeToCreate.description(),
                    productTypeToCreate.weight(),
                    productTypeToCreate.calories(),
                    productTypeToCreate.protein(),
                    productTypeToCreate.fats(),
                    productTypeToCreate.carbohydrates(),
                    productTypeToCreate.quantityInOnePackage(),
                    productTypeToCreate.mainImagePath(),
                    images,
                    shop
            );

            productTypeRepository.save(productTypeCreated);

            return getEntity(productTypeCreated);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public ProductTypeWithIdDto putEntity(ProductTypeWithIdDto productTypeToUpdate) {
        try {
            ProductType productTypeUpdated = productTypeConverter.toEntity(productTypeToUpdate.id());
            Shop shop = shopConverter.toEntity(productTypeToUpdate.shopId());
            List<Image> images = imageConverter.toEntityList(productTypeToUpdate.imagesId());

            productTypeUpdated.setName(productTypeToUpdate.name());
            productTypeUpdated.setDescription(productTypeToUpdate.description());
            productTypeUpdated.setWeight(productTypeToUpdate.weight());
            productTypeUpdated.setCalories(productTypeToUpdate.calories());
            productTypeUpdated.setProtein(productTypeToUpdate.protein());
            productTypeUpdated.setFats(productTypeToUpdate.fats());
            productTypeUpdated.setCarbohydrates(productTypeToUpdate.carbohydrates());
            productTypeUpdated.setQuantityInOnePackage(productTypeToUpdate.quantityInOnePackage());
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
        try {
            ProductType productTypeDeleted = productTypeConverter.toEntity(id);
            productTypeRepository.delete(productTypeDeleted);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
