package team.capybara.backend.spring.controllers.dto.product;


import team.capybara.backend.spring.controllers.dto.image.ImageDto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record ProductDto (
        UUID id,
        UUID productTypeId,
        String productTypeName,
        String productTypeDescription,
        String productTypeMainImagePath,
        List<ImageDto> productTypeImages,
        Date shelfLife,
        int price,
        int discount,
        boolean isSold
) {
}
