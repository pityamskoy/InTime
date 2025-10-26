package team.capybara.backend.spring.controllers.mapping;

import org.springframework.stereotype.Component;
import team.capybara.backend.spring.entitys.Image;
import team.capybara.backend.spring.entitys.Shop;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;
import team.capybara.backend.spring.controllers.dto.shop.ShopDto;

import java.util.List;
import java.util.UUID;

@Component
public class ShopMapper implements Mapper<Shop, ShopDto> {
    private final ImageMapper imageMapper = new ImageMapper();

    @Override
    public ShopDto toDto(Shop shop) {
        List<ImageDto> images = imageMapper.toDtoList(shop.getImages());

        return new ShopDto(
                shop.getId(),
                shop.getName(),
                shop.getDescription(),
                shop.isVerifide(),
                shop.getMainImagePath(),
                images,
                shop.getAddress(),
                shop.getLat(),
                shop.getLon()
        );
    }

    @Override
    public Shop postEntity(ShopDto shopDto) {
        List<Image> images = imageMapper.toEntityList(shopDto.images());

        return new Shop(
                UUID.randomUUID(),
                shopDto.name(),
                shopDto.description(),
                shopDto.isVerified(),
                shopDto.mainImagePath(),
                images,
                shopDto.address(),
                shopDto.lat(),
                shopDto.lon()
        );
    }
}
