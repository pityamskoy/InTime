package team.capybara.backend.spring.controllers.dto.entities.user;

import java.util.UUID;

public record UserAuthWithIdDto(
        UUID id,
        String name,
        String email,
        String phoneNumber,
        String password,
        Boolean isShopOwner
) {
}
