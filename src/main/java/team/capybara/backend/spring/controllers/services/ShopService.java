package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.capybara.backend.hibernate.Product;
import team.capybara.backend.spring.controllers.dto.product.ProductDto;
import team.capybara.backend.spring.controllers.mapping.ProductMapper;
import team.capybara.backend.spring.controllers.mapping.ShopMapper;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;

import java.util.Optional;

@Service
public class ShopService {
    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;
    private final ShopMapper shopMapper;
    private final ProductMapper productMapper;

    @Autowired
    public ShopService(
            ShopRepository shopRepository,
            ProductRepository productRepository,
            ShopMapper shopMapper,
            ProductMapper productMapper
    ) {
        this.shopRepository = shopRepository;
        this.productRepository = productRepository;
        this.shopMapper = shopMapper;
        this.productMapper = productMapper;
    }

    public Product createProduct(ProductDto productToCreate) {
        try {
            Product productToSave = productMapper.postEntity(productToCreate);
            return productRepository.save(productToSave);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e);
        }
    }

    public Product updateProduct(ProductDto productToUpdate) {
        Optional<Product> optionalProduct = productRepository.findById(productToUpdate.id());

        if (optionalProduct.isEmpty()) {
            throw new ServiceException(new EntityNotFoundException("Not found product by id=" + productToUpdate.id()));
        } else {
            Product product = optionalProduct.get();

            product.setShelfLife(productToUpdate.shelfLife());
            product.setPrice(productToUpdate.price());
            product.setDiscount(productToUpdate.discount());
            product.setIsSold(productToUpdate.isSold());

            return productRepository.save(product);
        }
    }

    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new ServiceException(new EntityNotFoundException("Not found product by id=" + id));
        }

        productRepository.deleteById(id);
    }
}
