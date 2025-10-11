package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.capybara.backend.hibernate.Product;
import team.capybara.backend.spring.controllers.dto.product.ProductDto;
import team.capybara.backend.spring.controllers.mapping.ProductMapper;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    ProductService(
            ProductRepository productRepository,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(productMapper::toDto).toList();
    }

    public Optional<ProductDto> getProductById(String id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat.format("Not found product by id={0}", id));
        }

        return Optional.ofNullable(productMapper.toDto(product.get()));
    }
    
    public Product createProduct(ProductDto productToCreate) throws EntityNotFoundException {
        Product productToSave = productMapper.toEntity(productToCreate);

        return productRepository.save(productToSave);
    }

    public Product updateProduct(
            ProductDto productToUpdate
    ) throws EntityNotFoundException {
        Product productToSave = productMapper.toEntity(productToUpdate);

        productToSave.setShelfLife(productToUpdate.shelfLife());
        productToSave.setPrice(productToUpdate.price());
        productToSave.setDiscount(productToUpdate.discount());
        productToSave.setIsSold(productToUpdate.isSold());

        return productRepository.save(productToSave);
    }

    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Not found product by id=" + id);
        }

        productRepository.deleteById(id);
    }
}
