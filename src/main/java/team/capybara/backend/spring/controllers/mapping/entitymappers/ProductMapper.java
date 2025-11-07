package team.capybara.backend.spring.controllers.mapping.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mapping.Mapper;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;
import team.capybara.backend.spring.entities.Product;
import team.capybara.backend.spring.entities.ProductType;
import team.capybara.backend.spring.controllers.dto.product.ProductDto;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;

import java.text.MessageFormat;
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
    public Product postEntity(ProductDto productDto) {
        UUID productTypeId = productDto.productTypeId();
        Optional<ProductType> productType = productTypeRepository.findById(productTypeId);

        if (productType.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat.format("Not found productType id={0}", productTypeId));
        }

        return new Product(
                UUID.randomUUID(),
                productType.get(),
                productDto.shelfLife(),
                productDto.price(),
                productDto.discount(),
                productDto.isSold()
        );
    }

    @Override
    public Product putEntity(ProductDto dtoObject) {
        Optional<Product> product = productRepository.findById(dtoObject.id());
        if (product.isEmpty()) {
            throw new EntityNotFoundException("Not found product id=" + dtoObject.productTypeId());
        }

        Optional<ProductType> productType = productTypeRepository.findById(dtoObject.productTypeId());
        if (productType.isEmpty()) {
            throw new EntityNotFoundException("Not found productType id=" + dtoObject.productTypeId());
        }

        Product obj = product.get();
        obj.setProductType(productType.get());
        obj.setShelfLife(dtoObject.shelfLife());
        obj.setPrice(dtoObject.price());
        obj.setDiscount(dtoObject.discount());
        obj.setSold(dtoObject.isSold());

        return obj;
    }

    @Override
    public void deleteEntity(UUID entityId) {
        Optional<Product> product = productRepository.findById(entityId);

        if (product.isEmpty()) {
            throw new EntityNotFoundException("Not found product id=" + entityId);
        }

        productRepository.delete(product.get());
    }
}
