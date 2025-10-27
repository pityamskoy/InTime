package team.capybara.backend.spring.controllers.mapping;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.entitys.Product;
import team.capybara.backend.spring.entitys.ProductType;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;
import team.capybara.backend.spring.controllers.dto.product.ProductDto;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Component
public class ProductMapper implements Mapper<Product, ProductDto> {
    private final ProductTypeRepository productTypeRepository;
    private final ImageMapper imageMapper = new ImageMapper();

    @Autowired
    public ProductMapper(
            ProductTypeRepository productTypeRepository
    ) {
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public ProductDto toDto(Product product) {
        ProductType productType = product.getProductType();
        List<ImageDto> images = imageMapper.toDtoList(productType.getImages());

        return new ProductDto(
                product.getId(),
                productType.getId(),
                productType.getName(),
                productType.getDescription(),
                productType.getMainImagePath(),
                images,
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
}
