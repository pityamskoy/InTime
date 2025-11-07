package team.capybara.backend.spring.controllers.dto.producttype;

import java.util.List;
import java.util.UUID;

public record ProductTypeDto(
        UUID id,
        String name,
        String description,
        String mainImagePath,
        List<String> imagesId,
        UUID shopId
) {
}
