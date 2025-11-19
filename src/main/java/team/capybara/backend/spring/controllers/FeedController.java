package team.capybara.backend.spring.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.filters.FeedFilterEntity;
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
    private static final int NUMBER_OF_PRODUCTS_PER_PAGE = 30;

    private final ProductService productService;

    public FeedController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{offset}")
    public ResponseEntity<Page<ProductDto>> getAllProducts(@PathVariable int offset) {
        log.info("Called getAllProducts");

        return ResponseEntity.ok(productService.getAllProducts(offset, NUMBER_OF_PRODUCTS_PER_PAGE));
    }

    @GetMapping("/filtered/{offset}")
    public ResponseEntity<Page<ProductDto>> getAllSortedProducts(
            @PathVariable int offset,
            @RequestBody FeedFilterEntity filter
    ) {
        if (filter.getShopsId() == null && filter.getCategoriesId() == null && !filter.isOnlyFreeProducts() && filter.getDistance() == 0) {
            return getAllProducts(offset);
        }

        log.info("Called getAllSortedProducts; filter={}", filter);

        return ResponseEntity.ok(productService.getAllSortedProducts(offset, NUMBER_OF_PRODUCTS_PER_PAGE, filter));
    }

    @GetMapping("product/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String id) {
        log.info("Called getProduct; id={}", id);
        Optional<ProductDto> productDtoOptional = productService.getProductById(UUID.fromString(id));

        return productDtoOptional.map(ResponseEntity::ok).orElseGet
                (() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<ProductDto>> getProductByName(@PathVariable String name) {
        log.info("Called getProduct; name={}", name);
        return ResponseEntity.ok(productService.getProductByName(name));
    }

    @PostMapping("/create")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productToCreate) {
        log.info("Called createProduct; productToCreate={}", productToCreate);

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(productService.createProduct(productToCreate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productToUpdate) {
        log.info("Called updateProduct; productToUpdate={}", productToUpdate);

        try {
            return ResponseEntity.ok(productService.updateProduct(productToUpdate));
        } catch (EntityNotFoundException e) {
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
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
