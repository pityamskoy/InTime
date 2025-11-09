package team.capybara.backend.spring.controllers.mappers.converters.entityconverters;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mappers.converters.EntityListIdConverter;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;
import team.capybara.backend.spring.entities.ProductType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public final class ProductTypeConverter implements EntityListIdConverter<ProductType> {
    private final ProductTypeRepository productTypeRepository;

    public ProductTypeConverter(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public List<UUID> toIdList(List<ProductType> productTypes) {
        List<UUID> productTypesId = new ArrayList<>();

        for (ProductType productType : productTypes) {
            productTypesId.add(productType.getId());
        }

        return productTypesId;
    }

    @Override
    public List<ProductType> toEntityList(List<UUID> entitiesId) {
        List<ProductType> productTypes = new ArrayList<>();

        for (UUID id : entitiesId) {
            Optional<ProductType> productTypeOptional = productTypeRepository.findById(id);

            if (productTypeOptional.isEmpty()) {
                throw new EntityNotFoundException("ProductType not found; id=" + id);
            }

            productTypes.add(productTypeOptional.get());
        }

        return productTypes;
    }

    @Override
    public ProductType toEntity(UUID id) {
        Optional<ProductType> productTypeOptional = productTypeRepository.findById(id);

        if (productTypeOptional.isEmpty()) {
            throw new EntityNotFoundException("ProductType not found; id=" + id);
        }

        return productTypeOptional.get();
    }
}
