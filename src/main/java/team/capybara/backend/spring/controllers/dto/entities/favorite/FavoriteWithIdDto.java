package team.capybara.backend.spring.controllers.dto.entities.favorite;

import java.util.UUID;

public record FavoriteWithIdDto(
        UUID id,
        UUID userId,
        UUID productTypeId
) {
}
