package team.capybara.backend.spring.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.mapping.entitymappers.ProductMapper;
import team.capybara.backend.spring.controllers.services.ServiceException;
import team.capybara.backend.spring.entities.*;
import team.capybara.backend.spring.controllers.dto.product.ProductDto;
import team.capybara.backend.spring.controllers.services.ProductService;

import java.util.*;

@RestController
@RequestMapping("/feed")
@CrossOrigin(value = {"http://localhost:3000"})
@SuppressWarnings(value = {"unused"})
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

        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String id) {
        log.info("Called getProduct; id={}", id);
        Optional<Product> product = productService.getProductById(UUID.fromString(id));

        return product.map(value -> ResponseEntity.ok(productMapper.getEntity(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productToCreate) {
        log.info("Called createProduct; productToCreate={}", productToCreate);

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(productService.createProduct(productToCreate));
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Product> updateProduct(@RequestBody ProductDto productToUpdate) {
        log.info("Called updateProduct; productToUpdate={}", productToUpdate);

        try {
            Product updated = productService.updateProduct(productToUpdate);
            return ResponseEntity.ok(updated);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    //Apparently, we aren't going to need it
    @Deprecated
    @PatchMapping("/{id}")
    public ResponseEntity<Product> partiallyUpdateProduct() {
        log.info("Called partiallyUpdateProduct");
        return null;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteProduct(@RequestBody String id) {
        log.info("Called deleteProduct; id={}", id);

        try {
            productService.deleteProduct(UUID.fromString(id));
            return ResponseEntity.ok().build();
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
