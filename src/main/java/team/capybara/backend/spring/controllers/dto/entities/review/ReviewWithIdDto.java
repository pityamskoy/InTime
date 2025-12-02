package team.capybara.backend.spring.controllers.dto.entities.review;

import java.util.UUID;

public record ReviewWithIdDto(
        UUID id,
        UUID userId,
        UUID shopId,
        String text,
        int stars
) {
}
