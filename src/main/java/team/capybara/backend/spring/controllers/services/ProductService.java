package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.entities.Product;
import team.capybara.backend.spring.controllers.dto.product.ProductDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.ProductMapper;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public final class ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public ProductService(
            ProductMapper productMapper,
            ProductRepository productRepository
    ) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(productMapper::getEntity).toList();
    }

    public Optional<ProductDto> getProductById(UUID id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            ProductDto productDto = productMapper.getEntity(productOptional.get());
            return Optional.of(productDto);
        }

        return Optional.empty();
    }

    public ProductDto createProduct(ProductDto productToCreate) {
        return productMapper.postEntity(productToCreate);
    }

    public ProductDto updateProduct(ProductDto productToUpdate) {
        try {
            return productMapper.putEntity(productToUpdate);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public void deleteProduct(UUID id) {
        try {
            productMapper.deleteEntity(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
