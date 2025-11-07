package team.capybara.backend.spring.controllers.mapping.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mapping.Mapper;
import team.capybara.backend.spring.controllers.repositories.ImageRepository;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;
import team.capybara.backend.spring.entities.Image;
import team.capybara.backend.spring.entities.ProductType;
import team.capybara.backend.spring.entities.Shop;
import team.capybara.backend.spring.controllers.dto.producttype.ProductTypeDto;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public final class ProductTypeMapper implements Mapper<ProductType, ProductTypeDto> {
    private final ProductTypeRepository productTypeRepository;
    private final ShopRepository shopRepository;
    private final ImageRepository imageRepository;


    public ProductTypeMapper(
            ProductTypeRepository productTypeRepository,
            ShopRepository shopRepository,
            ImageRepository imageRepository
    ) {
        this.productTypeRepository = productTypeRepository;
        this.shopRepository = shopRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public ProductTypeDto getEntity(ProductType productType) {
        List<Image> images = productType.getImages();
        List<String> imagesId = new ArrayList<>();

        for (Image image : images) {
            imagesId.add(image.getId().toString());
        }

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
    public ProductType postEntity(ProductTypeDto productTypeDto) {
        Optional<Shop> optionalShop = this.shopRepository.findById(productTypeDto.shopId());

        if (optionalShop.isEmpty()) {
            throw new EntityNotFoundException("Not found shop with id=" + productTypeDto.shopId());
        }

        List<String> imagesId = productTypeDto.imagesId();
        List<Image> images = new ArrayList<>();

        for (String imageId : imagesId) {
            Optional<Image> image = imageRepository.findById(UUID.fromString(imageId));

            if (image.isEmpty()) {
                throw new EntityNotFoundException("Not found image with id=" + imageId);
            }

            images.add(image.get());
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
    public ProductType putEntity(ProductTypeDto dtoObject) {
        Optional<ProductType> productType = productTypeRepository.findById(dtoObject.id());
        if (productType.isEmpty()) {
            throw new EntityNotFoundException("Not found product type with id=" + dtoObject.id());
        }

        Optional<Shop> shop = shopRepository.findById(dtoObject.shopId());
        if (shop.isEmpty()) {
            throw new EntityNotFoundException("Not found shop with id=" + dtoObject.shopId());
        }

        List<String> imagesId = dtoObject.imagesId();
        List<Image> images = new ArrayList<>();

        for (String imageId : imagesId) {
            Optional<Image> image = imageRepository.findById(UUID.fromString(imageId));

            if (image.isEmpty()) {
                throw new EntityNotFoundException("Not found image with id=" + imageId);
            }

            images.add(image.get());
        }

        ProductType obj = productType.get();
        obj.setName(dtoObject.name());
        obj.setDescription(dtoObject.description());
        obj.setMainImagePath(dtoObject.mainImagePath());
        obj.setImages(images);
        obj.setShop(shop.get());

        return obj;
    }


    @Override
    public void deleteEntity(UUID entityId) {
        Optional<ProductType> productType = productTypeRepository.findById(entityId);

        if (productType.isEmpty()) {
            throw new EntityNotFoundException("Not found product type with id=" + entityId);
        }

        productTypeRepository.deleteById(entityId);
    }
}
