package team.capybara.backend.spring.controllers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.capybara.backend.hibernate.Product;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProduct(String id) {
        return productRepository.findById(id);
    }
    
    public Product createProduct(Product productToCreate) {
        Product product = new Product(
                productToCreate.getProductType(),
                productToCreate.getShelfLife(),
                productToCreate.getDiscount(),
                productToCreate.getPrice(),
                productToCreate.getIsSold()
        );

        return productRepository.save(product);
    }

    public Product updateProduct(String id, Product productToUpdate) {
        return null;
    }

    public void deleteProduct(String id) {
        return;
    }
}
