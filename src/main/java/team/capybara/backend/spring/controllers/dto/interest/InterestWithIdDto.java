package team.capybara.backend.spring.controllers.dto.interest;

import java.util.Date;
import java.util.UUID;

public record InterestWithIdDto(
        UUID id,
        UUID userId,
        UUID productId,
        Date time
) {
}
