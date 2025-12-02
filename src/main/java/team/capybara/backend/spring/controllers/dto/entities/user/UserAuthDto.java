package team.capybara.backend.spring.controllers.dto.entities.user;

public record UserAuthDto(
        String name,
        String email,
        String phoneNumber,
        String password,
        Boolean isShopOwner
) {
}
