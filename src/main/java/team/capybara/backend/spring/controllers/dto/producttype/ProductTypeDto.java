package team.capybara.backend.spring.controllers.dto.producttype;

import team.capybara.backend.spring.controllers.dto.image.ImageDto;

import java.util.List;
import java.util.UUID;

public record ProductTypeDto(
        UUID id,
        String name,
        String description,
        String mainImagePath,
        List<ImageDto> imagesDto,
        UUID shopId
) {
}
