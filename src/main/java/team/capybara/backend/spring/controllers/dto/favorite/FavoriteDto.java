package team.capybara.backend.spring.controllers.dto.favorite;

import java.util.UUID;

public record FavoriteDto(
        UUID id,
        UUID userId,
        UUID productTypeId
) {
}
