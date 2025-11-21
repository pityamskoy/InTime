package team.capybara.backend.spring.controllers.dto.login;

public record LoginResultDto(
        Boolean success,
        String id
) {
}
