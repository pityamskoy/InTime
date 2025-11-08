package team.capybara.backend.spring.controllers.dto.image;

import java.util.UUID;

public record ImageDto(
        UUID imageId,
        String path
) {
}
