package team.capybara.backend.spring.controllers.dto.user;

import java.util.UUID;

public record UserAuthDto(
        UUID id,
        String name,
        String email,
        String password
) {
}
