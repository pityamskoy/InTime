package team.capybara.backend.spring.controllers.dto.entities.favorite;

import java.util.UUID;

public record FavoriteDto(
        UUID userId,
        UUID productTypeId
) {
}
