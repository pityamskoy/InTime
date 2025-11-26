package team.capybara.backend.spring.controllers.dto.other.login;

public record LoginResultDto(
        Boolean success,
        String userId
) {
}
