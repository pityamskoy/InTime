package team.capybara.backend.spring.controllers.mapping;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.entitys.Image;
import team.capybara.backend.spring.entitys.ProductType;
import team.capybara.backend.spring.entitys.Shop;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;
import team.capybara.backend.spring.controllers.dto.producttype.ProductTypeDto;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProductTypeMapper implements Mapper<ProductType, ProductTypeDto>{
    private final ImageMapper imageMapper = new ImageMapper();
    private final ShopRepository shopRepository;

    @Autowired
    public ProductTypeMapper(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }


    @Override
    public ProductTypeDto toDto(ProductType productType) {
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
}
