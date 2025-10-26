package team.capybara.backend.spring.controllers.dto.favorite;

import team.capybara.backend.spring.controllers.dto.producttype.ProductTypeDto;
import team.capybara.backend.spring.controllers.dto.user.UserDto;

import java.util.UUID;

public record FavoriteDto(
        UUID id,
        UserDto userDto,
        ProductTypeDto productTypeDto
) {
}
