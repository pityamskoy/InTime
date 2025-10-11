package team.capybara.backend.spring.controllers.dto.user;

public record UserAuthDto(
        String id,
        String name,
        String email,
        String password
) {
}
