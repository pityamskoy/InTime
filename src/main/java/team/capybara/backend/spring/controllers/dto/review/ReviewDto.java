package team.capybara.backend.spring.controllers.dto.review;

import java.util.UUID;

public record ReviewDto(
        UUID id,
        UUID userId,
        UUID shopId,
        String text,
        int stars
) {
}
