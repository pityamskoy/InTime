package team.capybara.backend.spring.controllers.dto.entities.user;

import java.util.UUID;

public record UserDto(
        UUID id,
        String name,
        String phoneNumber,
        Boolean isShopOwner
) {
}
