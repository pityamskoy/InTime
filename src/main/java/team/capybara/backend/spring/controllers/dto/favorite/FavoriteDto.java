package team.capybara.backend.spring.controllers.dto.favorite;

import team.capybara.backend.spring.controllers.dto.producttype.ProductTypeDto;
import team.capybara.backend.spring.controllers.dto.user.UserDto;

public record FavoriteDto(
        String id,
        UserDto userDto,
        ProductTypeDto productTypeDto
) {
}
