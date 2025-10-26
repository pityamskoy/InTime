package team.capybara.backend.spring.controllers.dto.review;

import team.capybara.backend.spring.controllers.dto.producttype.ProductTypeDto;
import team.capybara.backend.spring.controllers.dto.shop.ShopDto;
import team.capybara.backend.spring.controllers.dto.user.UserDto;

import java.util.UUID;

public record ReviewDto(
        UUID id,
        UserDto userDto,
        ShopDto shopDto,
        ProductTypeDto productTypeDto,
        String text,
        int stars
) {
}
