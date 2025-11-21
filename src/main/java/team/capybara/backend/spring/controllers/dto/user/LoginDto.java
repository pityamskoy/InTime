package team.capybara.backend.spring.controllers.dto.user;

public record LoginDto(
        String login,
        String password
) {
}
