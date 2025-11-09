package team.capybara.backend.spring.controllers.dto.category;

import java.util.List;
import java.util.UUID;

public record CategoryDto(
        UUID id,
        String name,
        String description,
        List<UUID> productTypesId
) {
}
