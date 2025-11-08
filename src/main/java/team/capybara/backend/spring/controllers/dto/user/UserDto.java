package team.capybara.backend.spring.controllers.dto.user;

import java.util.UUID;

public record UserDto(
        UUID id,
        String name
) {
}
