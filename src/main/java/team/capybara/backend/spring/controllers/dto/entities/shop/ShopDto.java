package team.capybara.backend.spring.controllers.dto.entities.shop;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record ShopDto(
        String name,
        Date timeOpen,
        Date timeClose,
        Date registrationDate,
        String description,
        boolean isVerified,
        String inn,
        String mainImagePath,
        List<UUID> imagesId,
        String address,
        Double lat,
        Double lon,
        String linkToSocialMedia,
        UUID owner,
        String companyType,
        Double distance
) {
}
