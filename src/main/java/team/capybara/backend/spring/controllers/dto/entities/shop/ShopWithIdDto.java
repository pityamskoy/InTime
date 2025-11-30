package team.capybara.backend.spring.controllers.dto.entities.shop;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public record ShopWithIdDto(
        UUID id,
        String name,
        Time timeOpen, //format 01:00:00
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
