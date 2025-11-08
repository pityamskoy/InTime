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
    public ProductType postEntity(ProductTypeDto productTypeToCreate) {
        Optional<Shop> optionalShop = this.shopRepository.findById(productTypeToCreate.shopId());

        if (optionalShop.isEmpty()) {
            throw new EntityNotFoundException("Not found shop with id=" + productTypeToCreate.shopId());
        }

        List<String> imagesId = productTypeToCreate.imagesId();
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
                productTypeToCreate.name(),
                productTypeToCreate.description(),
                productTypeToCreate.mainImagePath(),
                images,
                optionalShop.get()
        );
    }

    @Override
    public ProductType putEntity(ProductTypeDto productTypeToUpdate) {
        Optional<ProductType> productType = productTypeRepository.findById(productTypeToUpdate.id());
        if (productType.isEmpty()) {
            throw new EntityNotFoundException("Not found product type with id=" + productTypeToUpdate.id());
        }

        Optional<Shop> shop = shopRepository.findById(productTypeToUpdate.shopId());
        if (shop.isEmpty()) {
            throw new EntityNotFoundException("Not found shop with id=" + productTypeToUpdate.shopId());
        }

        List<String> imagesId = productTypeToUpdate.imagesId();
        List<Image> images = new ArrayList<>();

        for (String imageId : imagesId) {
            Optional<Image> image = imageRepository.findById(UUID.fromString(imageId));

            if (image.isEmpty()) {
                throw new EntityNotFoundException("Not found image with id=" + imageId);
            }
            images.add(image.get());
        }

        ProductType obj = productType.get();
        obj.setName(productTypeToUpdate.name());
        obj.setDescription(productTypeToUpdate.description());
        obj.setMainImagePath(productTypeToUpdate.mainImagePath());
        obj.setImages(images);
        obj.setShop(shop.get());

        return obj;
    }

    @Override
    public void deleteEntity(UUID id) {
        Optional<ProductType> productType = productTypeRepository.findById(id);

        if (productType.isEmpty()) {
            throw new EntityNotFoundException("Not found product type with id=" + id);
        }

        productTypeRepository.deleteById(id);
    }
}
