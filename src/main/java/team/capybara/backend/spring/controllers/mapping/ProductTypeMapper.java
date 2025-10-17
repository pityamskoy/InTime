package team.capybara.backend.spring.controllers.mapping;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.capybara.backend.hibernate.Image;
import team.capybara.backend.hibernate.ProductType;
import team.capybara.backend.hibernate.Shop;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;
import team.capybara.backend.spring.controllers.dto.producttype.ProductTypeDto;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
        List<ImageDto> images = new LinkedList<>();

        for (Image image : productType.getImages()) {
            images.add(this.imageMapper.toDto(image));
        }

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
    public ProductType toEntity(ProductTypeDto productTypeDto) {
        String shopId = productTypeDto.shopId();
        Optional<Shop> optionalShop = this.shopRepository.findById(shopId);

        if (optionalShop.isEmpty()) {
            throw new EntityNotFoundException("Not found shop with id: " + shopId);
        }

        List<Image> images = new LinkedList<>();
        for (ImageDto imageDto : productTypeDto.imagesDto()) {
            images.add(this.imageMapper.toEntity(imageDto));
        }

        return new ProductType(
                productTypeDto.name(),
                productTypeDto.description(),
                productTypeDto.mainImagePath(),
                images,
                optionalShop.get()
        );
    }
}
