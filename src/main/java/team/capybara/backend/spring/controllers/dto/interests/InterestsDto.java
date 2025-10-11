package team.capybara.backend.spring.controllers.dto.interests;

import team.capybara.backend.spring.controllers.dto.product.ProductDto;
import team.capybara.backend.spring.controllers.dto.user.UserDto;

import java.util.Date;

public record InterestsDto(
        String id,
        UserDto userDto,
        ProductDto productDto,
        Date time
) {
}
