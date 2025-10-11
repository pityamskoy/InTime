package team.capybara.backend.spring.controllers.dto.review;

import team.capybara.backend.spring.controllers.dto.producttype.ProductTypeDto;
import team.capybara.backend.spring.controllers.dto.shop.ShopDto;
import team.capybara.backend.spring.controllers.dto.user.UserDto;

public record ReviewDto(
        String id,
        UserDto userDto,
        ShopDto shopDto,
        ProductTypeDto productTypeDto,
        String text,
        int stars
) {
}
