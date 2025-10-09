package team.capybara.backend.spring.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.hibernate.Product;
import team.capybara.backend.hibernate.User;
import team.capybara.backend.spring.controllers.services.ProductService;
import team.capybara.backend.spring.controllers.services.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/feed")
public class FeedController{
    private static final Logger log = LoggerFactory.getLogger(FeedController.class);

    ProductService productService;

    @Autowired
    FeedController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("Called getAllProducts");
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Product>> getProduct(@PathVariable String id) {
        log.info("Called getProduct id={}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProduct(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product productToCreate) {
        log.info("Called createProduct product={}", productToCreate);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(productToCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable("id") String id,
            @RequestBody Product productToUpdate
        ) {
        log.info("Called updateProduct id={}, productToUpdate={}", id, productToUpdate);
        Product updated = productService.updateProduct(id, productToUpdate);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updated);
    }

    //fix
    @PatchMapping("/{id}")
    public ResponseEntity<Product> partiallyUpdateProduct() {
        log.info("Called partiallyUpdateProduct");
        return null;
    }

    //fix
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id) {
        log.info("Called deleteProduct id={}", id);

        try {
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
