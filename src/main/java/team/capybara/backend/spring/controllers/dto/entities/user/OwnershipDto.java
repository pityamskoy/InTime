package team.capybara.backend.spring.controllers.dto.entities.user;

import java.util.UUID;

public record OwnershipDto(
        Boolean isShopOwner,
        UUID shopId
) {
}
