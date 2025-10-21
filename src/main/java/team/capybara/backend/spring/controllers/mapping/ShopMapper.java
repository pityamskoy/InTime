package team.capybara.backend.spring.controllers.mapping;

import org.springframework.stereotype.Component;
import team.capybara.backend.hibernate.Image;
import team.capybara.backend.hibernate.Shop;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;
import team.capybara.backend.spring.controllers.dto.shop.ShopDto;

import java.util.List;

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
                shop.getIsVerifide(),
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
