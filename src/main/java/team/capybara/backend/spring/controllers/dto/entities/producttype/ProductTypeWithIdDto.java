package team.capybara.backend.spring.controllers.dto.entities.producttype;

import java.util.List;
import java.util.UUID;

public record ProductTypeWithIdDto(
        UUID id,
        String name,
        String description,
        Double weight,
        Double calories,
        Double protein,
        Double fats,
        Double carbohydrates,
        int quantityInOnePackage,
        UUID mainImage,
        List<UUID> imagesId,
        UUID shopId
) {
}
