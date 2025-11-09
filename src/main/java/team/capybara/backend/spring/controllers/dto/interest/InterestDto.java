package team.capybara.backend.spring.controllers.dto.interest;

import java.util.Date;
import java.util.UUID;

public record InterestDto(
        UUID id,
        UUID userId,
        UUID productId,
        Date time
) {
}
