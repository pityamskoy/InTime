package team.capybara.backend.spring.controllers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.capybara.backend.hibernate.Product;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> allProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> product(String id) {
        return productRepository.findById(id);
    }
}
