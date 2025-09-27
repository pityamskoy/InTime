package team.capybara.backend.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.capybara.backend.hibernate.Product;
import team.capybara.backend.spring.controllers.services.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/feed ")
public class FeedController{
    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<List<Product>>(productService.allProducts(), HttpStatus.OK);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<Optional<Product>> getProduct(@PathVariable String id) {
        return new ResponseEntity<Optional<Product>>(productService.product(id), HttpStatus.OK);
    }
}
