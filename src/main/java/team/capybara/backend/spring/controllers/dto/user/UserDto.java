package team.capybara.backend.spring.controllers.dto.user;

public record UserDto(
        String id,
        String name,
        String email
) {
}
