package team.capybara.backend.spring.controllers.dto.category;

import team.capybara.backend.spring.controllers.dto.producttype.ProductTypeDto;

import java.util.List;

public record CategoryDto(
        String id,
        String name,
        String description,
        List<ProductTypeDto> productTypes
) {
}
