package team.capybara.backend.spring.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.hibernate.Product;
import team.capybara.backend.spring.controllers.services.ProductService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/feed")
public class FeedController{
    private static final Logger logger = LoggerFactory.getLogger(FeedController.class);

    ProductService productService;

    @Autowired
    FeedController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<List<Product>> getAllProducts() {
        logger.info("Called getAllProducts");
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Product>> getProduct(@PathVariable String id) {
        logger.info("Called getProduct");
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProduct(id));
    }

    // define url to create products
    @PostMapping()
    public ResponseEntity<Product> createProduct(@RequestBody Product productToCreate) {
        logger.info("Called createProduct");
        //return ResponseEntity.status(HttpStatus.CREATED).build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(productToCreate));
    }

    //define url to update products
    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable("id") String id,
            @RequestBody Product productToUpdate
        ) {
        logger.info("Called updateProduct id={}, productToUpdate={}", id, productToUpdate);
        Product updated = productService.updateProduct(id, productToUpdate);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updated);
    }

    @DeleteMapping("/feed/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id) {
        logger.info("Called deleteProduct id={}", id);

        try {
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
