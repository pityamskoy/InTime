package team.capybara.backend.spring.controllers.dto.entities.image;

import java.util.UUID;

public record ImageWithIdDto(
        UUID id,
        String path
) {
}
