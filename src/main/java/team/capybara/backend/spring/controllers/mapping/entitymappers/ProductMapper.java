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
    private final ProductTypeRepository productTypeRepository;
    private final ProductRepository productRepository;
    private final ProductTypeMapper productTypeMapper;
    private final ImageMapper imageMapper;

    public ProductMapper(
            ProductTypeRepository productTypeRepository,
            ProductRepository productRepository,
            ImageMapper imageMapper,
            ProductTypeMapper productTypeMapper
    ) {
        this.productTypeRepository = productTypeRepository;
        this.productRepository = productRepository;
        this.imageMapper = imageMapper;
        this.productTypeMapper = productTypeMapper;
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
    public Product putEntity(ProductDto dtoObjectWithId) {/*
        Optional<Product> product = productRepository.findById(dtoObjectWithId.productTypeId());

        if  (product.isEmpty()) {
            throw new EntityNotFoundException("Not found product id=" + dtoObjectWithId.productTypeId());
        }

        Product obj = product.get();
        obj.setId(dtoObjectWithId.productTypeId());
        obj.setSold(dtoObjectWithId.isSold());
        obj.setPrice(dtoObjectWithId.price());
        obj.setDiscount(dtoObjectWithId.discount());
        obj.setShelfLife(dtoObjectWithId.shelfLife());
        obj.setPrice(dtoObjectWithId.price());

        return obj;*/
        return null;
    }

    @Override
    public void removeEntity(UUID entityId) {
        Optional<Product> product = productRepository.findById(entityId);

        if (product.isEmpty()) {
            throw new EntityNotFoundException("Not found product id=" + entityId);
        }

        productRepository.delete(product.get());
    }
}
