package team.capybara.backend.spring.controllers.dto.shop;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record ShopWithIdDto(
        UUID id,
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
