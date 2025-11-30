package team.capybara.backend.spring.controllers.dto.entities.shop;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public record ShopDto(
        String name,
        Time timeOpen,
        Time timeClose,
        Date registrationDate,
        String description,
        boolean isVerified,
        String inn,
        UUID mainImage,
        List<UUID> imagesId,
        String address,
        Double lat,
        Double lon,
        String linkToSocialMedia,
        UUID owner,
        String organizationPhoneNumber,
        String companyType,
        Double distance
) {
}
