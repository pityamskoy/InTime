package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.repositories.ImageRepository;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;
import team.capybara.backend.spring.entities.Image;
import team.capybara.backend.spring.entities.Product;
import team.capybara.backend.spring.controllers.dto.product.ProductDto;
import team.capybara.backend.spring.controllers.mapping.entitymappers.ProductMapper;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ShopRepository shopRepository;
    private final ImageRepository imageRepository;

    private final ProductMapper productMapper;

    public ProductService(
            ProductRepository productRepository,
            ProductMapper productMapper,
            ProductTypeRepository productTypeRepository,
            ShopRepository shopRepository,
            ImageRepository imageRepository
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productTypeRepository = productTypeRepository;
        this.shopRepository = shopRepository;
        this.imageRepository = imageRepository;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(productMapper::getEntity).toList();
    }

    public Optional<ProductDto> getProductById(String id) {
        Optional<Product> product = productRepository.findById(UUID.fromString(id));

        if (product.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat.format("Not found product by id={0}", UUID.fromString(id)));
        }

        return Optional.ofNullable(productMapper.getEntity(product.get()));
    }

    public Product createProduct(ProductDto productToCreate) {
        Product productToSave = productMapper.postEntity(productToCreate);

        for (Image img : productToSave.getProductType().getImages()) {
            imageRepository.save(img);
        }
        for (Image img : productToSave.getProductType().getShop().getImages()) {
            imageRepository.save(img);
        }
        shopRepository.save(productToSave.getProductType().getShop());

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
