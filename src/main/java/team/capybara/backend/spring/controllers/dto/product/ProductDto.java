package team.capybara.backend.spring.controllers.dto.product;

import java.util.Date;
import java.util.UUID;

public record ProductDto(
        UUID id,
        UUID productTypeId,
        Date shelfLife,
        int price,
        int discount,
        boolean isSold
) {
}
