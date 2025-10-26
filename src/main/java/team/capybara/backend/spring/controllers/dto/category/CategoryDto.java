package team.capybara.backend.spring.controllers.dto.category;

import team.capybara.backend.spring.controllers.dto.producttype.ProductTypeDto;

import java.util.List;
import java.util.UUID;

public record CategoryDto(
        UUID id,
        String name,
        String description,
        List<ProductTypeDto> productTypes
) {
}
