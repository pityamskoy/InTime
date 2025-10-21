package team.capybara.backend.spring.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.hibernate.Product;
import team.capybara.backend.spring.controllers.dto.product.ProductDto;
import team.capybara.backend.spring.controllers.services.ProductService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@RestController
@CrossOrigin(value = {"http://localhost:3000"})
@RequestMapping("/feed")
public class FeedController{
    private static final Logger log = LoggerFactory.getLogger(FeedController.class);

    ProductService productService;

    @Autowired
    public FeedController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        log.info("Called getAllProducts");

        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ProductDto>> getProductById(@PathVariable String id) {
        log.info("Called getProduct id={}", id);

        try {
            Optional<ProductDto> productDto = productService.getProductById(id);
            return ResponseEntity.status(HttpStatus.OK).body(productDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productToCreate) {
        log.info("Called createProduct product={}", productToCreate);

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(productService.createProduct(productToCreate));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @RequestBody ProductDto productToUpdate
        ) {
        log.info("Called updateProduct id={}, productToUpdate={}", productToUpdate.id(), productToUpdate);

        try {
            Product updated = productService.updateProduct(productToUpdate);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //fix
    @PatchMapping("/{id}")
    public ResponseEntity<Product> partiallyUpdateProduct() {
        log.info("Called partiallyUpdateProduct");
        return null;
    }

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
