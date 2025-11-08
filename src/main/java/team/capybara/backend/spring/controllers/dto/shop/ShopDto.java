package team.capybara.backend.spring.controllers.dto.shop;

import java.util.List;
import java.util.UUID;

public record ShopDto(
        UUID id,
        String name,
        String description,
        boolean isVerified,
        String mainImagePath,
        List<UUID> imagesId,
        String address,
        Double lat,
        Double lon
) {
}
