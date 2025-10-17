package team.capybara.backend.spring.controllers.dto.product;


import team.capybara.backend.spring.controllers.dto.image.ImageDto;

import java.util.Date;
import java.util.List;

public record ProductDto (
        String id,
        String productTypeId,
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
