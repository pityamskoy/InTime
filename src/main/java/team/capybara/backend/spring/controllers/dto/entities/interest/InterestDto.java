package team.capybara.backend.spring.controllers.dto.entities.interest;

import java.util.Date;
import java.util.UUID;

public record InterestDto(
        UUID userId,
        UUID productId,
        Date time
) {
}
