package team.capybara.backend.spring.controllers.mapping.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mapping.Mapper;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;
import team.capybara.backend.spring.entities.Product;
import team.capybara.backend.spring.entities.ProductType;
import team.capybara.backend.spring.controllers.dto.product.ProductDto;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;

import java.util.Optional;
import java.util.UUID;


@Component
public final class ProductMapper implements Mapper<Product, ProductDto> {
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;

    public ProductMapper(
            ProductRepository productRepository,
            ProductTypeRepository productTypeRepository
    ) {
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public ProductDto getEntity(Product product) {
        UUID productTypeId = product.getProductType().getId();

        return new ProductDto(
                product.getId(),
                productTypeId,
                product.getShelfLife(),
                product.getPrice(),
                product.getDiscount(),
                product.isSold()
        );
    }

    @Override
    public ProductDto postEntity(ProductDto productToCreate) {
        Optional<ProductType> productType = productTypeRepository.findById(productToCreate.productTypeId());

        if (productType.isEmpty()) {
            throw new EntityNotFoundException("ProductType not found; id=" + productToCreate.productTypeId());
        }

        Product productCreated = productRepository.save(new Product(
                UUID.randomUUID(),
                productType.get(),
                productToCreate.shelfLife(),
                productToCreate.price(),
                productToCreate.discount(),
                productToCreate.isSold()
        ));

        return getEntity(productCreated);
    }

    @Override
    public ProductDto putEntity(ProductDto productToUpdate) {
        Optional<Product> product = productRepository.findById(productToUpdate.id());
        if (product.isEmpty()) {
            throw new EntityNotFoundException("Not found product; id=" + productToUpdate.productTypeId());
        }

        Optional<ProductType> productType = productTypeRepository.findById(productToUpdate.productTypeId());
        if (productType.isEmpty()) {
            throw new EntityNotFoundException("Not found productType; id=" + productToUpdate.productTypeId());
        }

        Product obj = product.get();
        obj.setProductType(productType.get());
        obj.setShelfLife(productToUpdate.shelfLife());
        obj.setPrice(productToUpdate.price());
        obj.setDiscount(productToUpdate.discount());
        obj.setSold(productToUpdate.isSold());
        productRepository.save(obj);

        return getEntity(obj);
    }

    @Override
    public void deleteEntity(UUID id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new EntityNotFoundException("Not found product; id=" + id);
        }

        productRepository.delete(product.get());
    }
}
