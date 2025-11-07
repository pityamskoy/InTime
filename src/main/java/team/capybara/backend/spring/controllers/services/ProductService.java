package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.repositories.ImageRepository;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;
import team.capybara.backend.spring.entities.Product;
import team.capybara.backend.spring.controllers.dto.product.ProductDto;
import team.capybara.backend.spring.controllers.mapping.entitymappers.ProductMapper;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public final class ProductService {
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final ImageRepository imageRepository;

    private final ProductMapper productMapper;

    public ProductService(
            ProductRepository productRepository,
            ProductMapper productMapper,
            ShopRepository shopRepository,
            ImageRepository imageRepository
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.shopRepository = shopRepository;
        this.imageRepository = imageRepository;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(productMapper::getEntity).toList();
    }

    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    public Product createProduct(ProductDto productToCreate) {
        Product productToSave = productMapper.postEntity(productToCreate);

        return productRepository.save(productToSave);
    }

    public Product updateProduct(ProductDto productDto) {
        try {
            return productMapper.putEntity(productDto);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteProduct(UUID id) {
        try {
            imageRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
