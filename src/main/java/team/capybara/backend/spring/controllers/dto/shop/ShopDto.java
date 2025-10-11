package team.capybara.backend.spring.controllers.dto.shop;

import team.capybara.backend.hibernate.Image;

import java.util.List;

public record ShopDto(
        String id,
        String name,
        String description,
        boolean isVerified,
        String mainImagePath,
        List<Image> images,
        String address,
        Double lat,
        Double lon
) {
}
