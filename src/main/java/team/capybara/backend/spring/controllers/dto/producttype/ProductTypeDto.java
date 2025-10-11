package team.capybara.backend.spring.controllers.dto.producttype;

import team.capybara.backend.spring.controllers.dto.image.ImageDto;
import team.capybara.backend.spring.controllers.dto.shop.ShopDto;

import java.util.List;

public record ProductTypeDto(
        String id,
        String name,
        String description,
        String mainImagePath,
        List<ImageDto> imagesDto,
        ShopDto shopDto
) {
}
