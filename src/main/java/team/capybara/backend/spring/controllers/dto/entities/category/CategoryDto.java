package team.capybara.backend.spring.controllers.dto.entities.category;

import java.util.List;
import java.util.UUID;

public record CategoryDto(
        String name,
        String description,
        List<UUID> productTypesId
) {
}
