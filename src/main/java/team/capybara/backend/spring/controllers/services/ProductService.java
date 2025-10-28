package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.repositories.ImageRepository;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;
import team.capybara.backend.spring.entitys.Image;
import team.capybara.backend.spring.entitys.Product;
import team.capybara.backend.spring.controllers.dto.product.ProductDto;
import team.capybara.backend.spring.controllers.mapping.ProductMapper;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductTypeRepository productTypeRepository;
    private final ShopRepository shopRepository;
    private final ImageRepository imageRepository;

    @Autowired
    ProductService(
            ProductRepository productRepository,
            ProductMapper productMapper, ProductTypeRepository productTypeRepository, ShopRepository shopRepository, ImageRepository imageRepository
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productTypeRepository = productTypeRepository;
        this.shopRepository = shopRepository;
        this.imageRepository = imageRepository;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(productMapper::toDto).toList();
    }

    public Optional<ProductDto> getProductById(String id) {
        Optional<Product> product = productRepository.findById(UUID.fromString(id));

        if (product.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat.format("Not found product by id={0}", UUID.fromString(id)));
        }

        return Optional.ofNullable(productMapper.toDto(product.get()));
    }

    public Product createProduct(ProductDto productToCreate) throws EntityNotFoundException {
        Product productToSave = productMapper.postEntity(productToCreate);

        for (Image img : productToSave.getProductType().getImages()) {
            imageRepository.save(img);
        }
        for (Image img : productToSave.getProductType().getShop().getImages()) {
            imageRepository.save(img);
        }
        shopRepository.save(productToSave.getProductType().getShop());
        productTypeRepository.save(productToSave.getProductType());

        return productRepository.save(productToSave);
    }

    public Product createProduct(Product productToCreate) throws EntityNotFoundException {

        //рабочий вариант без дто
        for (Image img : productToCreate.getProductType().getImages()) {
            imageRepository.save(img);
        }
        for (Image img : productToCreate.getProductType().getShop().getImages()) {
            imageRepository.save(img);
        }
        shopRepository.save(productToCreate.getProductType().getShop());
        productTypeRepository.save(productToCreate.getProductType());

        return productRepository.save(productToCreate);
    }

    public Product updateProduct(
            ProductDto productToUpdate
    ) throws EntityNotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(productToUpdate.id());

        if (optionalProduct.isEmpty()) {
            throw new EntityNotFoundException("Not found product by id=" + productToUpdate.id());
        } else {
            Product product = optionalProduct.get();

            product.setShelfLife(productToUpdate.shelfLife());
            product.setPrice(productToUpdate.price());
            product.setDiscount(productToUpdate.discount());
            product.setSold(productToUpdate.isSold());

            return productRepository.save(product);
        }
    }

    public void deleteProduct(String id) {
        if (!productRepository.existsById(UUID.fromString(id))) {
            throw new EntityNotFoundException("Not found product by id=" + id);
        }

        productRepository.deleteById(UUID.fromString(id));
    }
}
