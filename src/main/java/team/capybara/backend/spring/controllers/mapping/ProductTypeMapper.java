package team.capybara.backend.spring.controllers.mapping;

import team.capybara.backend.hibernate.ProductType;
import team.capybara.backend.spring.controllers.dto.producttype.ProductTypeDto;

public class ProductTypeMapper implements Mapper<ProductType, ProductTypeDto>{
    @Override
    public ProductTypeDto toDto(ProductType productType) {
        return null;
    }

    @Override
    public ProductType toEntity(ProductTypeDto productTypeDto) {
        return null;
    }
}
