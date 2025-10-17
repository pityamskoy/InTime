package team.capybara.backend.spring.controllers.mapping;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.capybara.backend.hibernate.Image;
import team.capybara.backend.hibernate.Product;
import team.capybara.backend.hibernate.ProductType;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;
import team.capybara.backend.spring.controllers.dto.product.ProductDto;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


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
        List<ImageDto> images = new LinkedList<>();

        for (Image image : productType.getImages()) {
            images.add(this.imageMapper.toDto(image));
        }

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
                product.getIsSold()
        );
    }

    @Override
    public Product toEntity(ProductDto productDto) {
        String productTypeId = productDto.productTypeId();
        Optional<ProductType> productType = productTypeRepository.findById(productTypeId);

        if (productType.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat.format("Not found productType id={0}", productTypeId));
        }

        return new Product(
                productType.get(),
                productDto.shelfLife(),
                productDto.price(),
                productDto.discount(),
                productDto.isSold()
        );
    }
}
