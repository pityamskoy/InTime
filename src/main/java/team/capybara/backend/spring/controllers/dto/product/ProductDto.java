package team.capybara.backend.spring.controllers.dto.product;


import java.util.Date;

public record ProductDto (
        String id,
        String productTypeId,
        String productTypeName,
        String productTypeDescription,
        Date shelfLife,
        int price,
        int discount,
        boolean isSold
) {
}
