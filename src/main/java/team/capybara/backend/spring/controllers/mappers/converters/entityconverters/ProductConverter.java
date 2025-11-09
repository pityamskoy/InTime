package team.capybara.backend.spring.controllers.mappers.converters.entityconverters;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mappers.converters.EntityIdConverter;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;
import team.capybara.backend.spring.entities.Product;

import java.util.Optional;
import java.util.UUID;

@Component
public final class ProductConverter implements EntityIdConverter<Product> {
    private final ProductRepository productRepository;

    public ProductConverter(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product toEntity(UUID id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty()) {
            throw new EntityNotFoundException("Product not found; id=" + id);
        }

        return productOptional.get();
    }
}
