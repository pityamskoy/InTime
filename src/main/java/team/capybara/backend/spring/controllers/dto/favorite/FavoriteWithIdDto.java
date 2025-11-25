package team.capybara.backend.spring.controllers.dto.favorite;

import java.util.UUID;

public record FavoriteWithIdDto(
        UUID id,
        UUID userId,
        UUID productTypeId
) {
}
