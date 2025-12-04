package team.capybara.backend.spring.controllers.mappers.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.dto.entities.product.ProductDto;
import team.capybara.backend.spring.controllers.mappers.Mapper;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ProductConverter;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ProductTypeConverter;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;
import team.capybara.backend.spring.entities.Product;
import team.capybara.backend.spring.entities.ProductType;
import team.capybara.backend.spring.controllers.dto.entities.product.ProductWithIdDto;

import java.util.Date;
import java.util.UUID;


@Component
public final class ProductMapper implements Mapper<Product, ProductWithIdDto, ProductDto> {
    private final ProductRepository productRepository;
    private final ProductConverter productConverter;
    private final ProductTypeConverter productTypeConverter;

    public ProductMapper(
            ProductRepository productRepository,
            ProductConverter productConverter,
            ProductTypeConverter productTypeConverter
    ) {
        this.productRepository = productRepository;
        this.productConverter = productConverter;
        this.productTypeConverter = productTypeConverter;
    }

    @Override
    public ProductWithIdDto getEntity(Product product) {
        UUID productTypeId = product.getProductType().getId();

        return new ProductWithIdDto(
                product.getId(),
                productTypeId,
                product.getShelfLife(),
                product.getPrice(),
                product.getDiscount(),
                product.isSold()
        );
    }

    @Override
    public ProductWithIdDto postEntity(ProductDto productToCreate) {
        try {
            ProductType productType = productTypeConverter.toEntity(productToCreate.productTypeId());

            try {
                throwExceptionIfFieldsAreIncorrect(productToCreate.shelfLife(), productToCreate.price(), productToCreate.discount());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage());
            }

            Product productCreated = productRepository.save(new Product(
                    UUID.randomUUID(),
                    productType,
                    productToCreate.shelfLife(),
                    productToCreate.price(),
                    productToCreate.discount(),
                    productToCreate.isSold(),
                    0
            ));

            return getEntity(productCreated);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public ProductWithIdDto putEntity(ProductWithIdDto productToUpdate) {
        try {
            Product productUpdated = productConverter.toEntity(productToUpdate.id());
            ProductType productType = productTypeConverter.toEntity(productToUpdate.id());

            try {
                throwExceptionIfFieldsAreIncorrect(productToUpdate.shelfLife(), productToUpdate.price(), productToUpdate.discount());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage());
            }

            productUpdated.setProductType(productType);
            productUpdated.setShelfLife(productToUpdate.shelfLife());
            productUpdated.setPrice(productToUpdate.price());
            productUpdated.setDiscount(productToUpdate.discount());
            productUpdated.setSold(productToUpdate.isSold());
            productRepository.save(productUpdated);

            return getEntity(productUpdated);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public void deleteEntity(UUID id) {
        try {
            Product productDeleted = productConverter.toEntity(id);
            productRepository.delete(productDeleted);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    //add check for shelfLife
    private void throwExceptionIfFieldsAreIncorrect(Date shelfLife, int price, int discount) {
        boolean isFieldsCorrect = price >= 0;

        if (discount < 0 || discount > 100) {
            isFieldsCorrect = false;
        }

        if (price - price * discount / 100 < 0) {
            isFieldsCorrect = false;
        }

        if (!isFieldsCorrect) {
            throw new IllegalArgumentException("Fields are incorrect.");
        }
    }
}
