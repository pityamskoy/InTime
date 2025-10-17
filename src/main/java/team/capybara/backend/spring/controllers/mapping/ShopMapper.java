package team.capybara.backend.spring.controllers.mapping;

import org.springframework.stereotype.Component;
import team.capybara.backend.hibernate.Shop;
import team.capybara.backend.spring.controllers.dto.shop.ShopDto;

@Component
public class ShopMapper implements Mapper<Shop, ShopDto> {

    @Override
    public ShopDto toDto(Shop shop) {
        return null; /*
        return new ShopDto(
                shop.getId(),
                shop.getName(),
                shop.
        )*/
    }

    @Override
    public Shop toEntity(ShopDto shopDto) {
        return null;
    }
}
