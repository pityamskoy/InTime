package team.capybara.backend.spring.controllers.dto.image;

import java.util.UUID;

public record ImageWithIdDto(
        UUID id,
        String path
) {
}
