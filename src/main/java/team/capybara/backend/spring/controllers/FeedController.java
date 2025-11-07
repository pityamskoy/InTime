package team.capybara.backend.spring.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.mapping.entitymappers.ProductMapper;
import team.capybara.backend.spring.entities.*;
import team.capybara.backend.spring.controllers.dto.product.ProductDto;
import team.capybara.backend.spring.controllers.services.ProductService;

import java.util.*;

@RestController
@CrossOrigin(value = {"http://localhost:3000"})
@RequestMapping("/feed")
public final class FeedController{
    private static final Logger log = LoggerFactory.getLogger(FeedController.class);
    private final ProductService productService;
    private final ProductMapper productMapper;

    public FeedController(
            ProductService productService,
            ProductMapper productMapper
    ) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        log.info("Called getAllProducts");

        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String id) {
        log.info("Called getProduct id={}", id);

        Optional<Product> product = productService.getProductById(UUID.fromString(id));
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(productMapper.getEntity(product.get()));
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
            productService.deleteProduct(UUID.fromString(id));
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
