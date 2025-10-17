package team.capybara.backend.spring.controllers.dto.shop;

import team.capybara.backend.spring.controllers.dto.image.ImageDto;

import java.util.List;

public record ShopDto(
        String id,
        String name,
        String description,
        boolean isVerified,
        String mainImagePath,
        List<ImageDto> images,
        String address,
        Double lat,
        Double lon
) {
}
