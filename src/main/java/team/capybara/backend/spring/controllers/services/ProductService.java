package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.entities.product.ProductDto;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;
import team.capybara.backend.spring.entities.Product;
import team.capybara.backend.spring.controllers.dto.entities.product.ProductWithIdDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.ProductMapper;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;
import team.capybara.backend.spring.entities.ProductType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public final class ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;

    public ProductService(
            ProductMapper productMapper,
            ProductRepository productRepository,
            ProductTypeRepository productTypeRepository
    ) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
    }

    public Page<ProductWithIdDto> getAllProducts(int offset, int limit) {
        Page<Product> products = productRepository.findAll(PageRequest.of(offset, limit));
        products.stream().forEach(product -> product.calculateScore(1, 5));
        return products.map(productMapper::getEntity);
    }

    public Optional<ProductWithIdDto> getProductById(UUID id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            ProductWithIdDto productWithIdDto = productMapper.getEntity(productOptional.get());
            return Optional.of(productWithIdDto);
        }

        return Optional.empty();
    }

    public Page<ProductWithIdDto> getProductByShop(int offset, int limit, String id) {

        List<ProductType>productTypes = productTypeRepository.findAll();
        List<ProductType>productTypesWithNeededShop = new ArrayList<>();
        for(ProductType productType : productTypes){
            if(productType.getShop().getId().equals(UUID.fromString(id)))
                productTypesWithNeededShop.add(productType);
        }
        Page<Product> products = productRepository.findByProductTypeIn(productTypesWithNeededShop,PageRequest.of(offset, limit));
        products.stream().forEach(product -> product.calculateScore(1,5));
        return products.map(productMapper::getEntity);
    }

    public ProductWithIdDto createProduct(ProductDto productToCreate) {
        return productMapper.postEntity(productToCreate);
    }

    public ProductWithIdDto updateProduct(ProductWithIdDto productToUpdate) {
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
