package team.capybara.backend.spring.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.entities.product.ProductDto;
import team.capybara.backend.spring.controllers.dto.other.filters.FeedFilterEntity;
import team.capybara.backend.spring.controllers.dto.other.pagination.PaginationLimit;
import team.capybara.backend.spring.controllers.services.FilteredProductService;
import team.capybara.backend.spring.entities.*;
import team.capybara.backend.spring.controllers.dto.entities.product.ProductWithIdDto;
import team.capybara.backend.spring.controllers.services.ProductService;

import java.util.*;

@RestController
@RequestMapping("/feed")
@CrossOrigin(value = {"http://localhost:3000"})
@SuppressWarnings(value = {"unused"})
public final class FeedController{
    private static final Logger log = LoggerFactory.getLogger(FeedController.class);

    private final ProductService productService;
    private final FilteredProductService filteredProductService;

    public FeedController(
            ProductService productService,
            FilteredProductService filteredProductService
    ) {
        this.productService = productService;
        this.filteredProductService = filteredProductService;
    }

    @PostMapping("/{offset}")
    public ResponseEntity<Page<ProductWithIdDto>> getAllProducts(
            @PathVariable int offset,
            @RequestBody PaginationLimit limit
    ) {
        log.info("Called getAllProducts; offset={}, limit={}", offset, limit.getLimit());

        return ResponseEntity.ok(productService.getAllProducts(offset, limit.getLimit()));
    }

    @PostMapping("/products_by_shop/{id}/{offset}")
    public ResponseEntity<Page<ProductWithIdDto>> getProductsByShopId(
            @PathVariable String id,
            @PathVariable int offset,
            @RequestBody PaginationLimit limit
    ) {
        log.info("Called getProductsByShopId; id={}, offset={}, limit={}", id, offset, limit.getLimit());

        return ResponseEntity.ok(productService.getProductByShop(offset, limit.getLimit(), id));
    }

    @PostMapping("/filtered/{offset}")
    public ResponseEntity<Page<ProductWithIdDto>> getAllSortedProducts(
            @PathVariable int offset,
            @RequestBody FeedFilterEntity filter
    ) {
        if (filter.getShopsId() == null && filter.getCategoriesId() == null && filter.getIsOnlyFreeProducts() == null
                && filter.getDistance() == null && filter.getUserId() == null) {
            return getAllProducts(offset, new PaginationLimit(filter.getLimit()));
        }

        log.info("Called getAllSortedProducts; filter={}", filter);

        return ResponseEntity.ok(filteredProductService.getAllSortedProducts(offset, filter));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductWithIdDto> getProductById(@PathVariable String id) {
        log.info("Called getProduct; id={}", id);
        Optional<ProductWithIdDto> productDtoOptional = productService.getProductById(UUID.fromString(id));

        return productDtoOptional.map(ResponseEntity::ok).orElseGet
                (() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<ProductWithIdDto> createProduct(@RequestBody ProductDto productToCreate) {
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
    public ResponseEntity<ProductWithIdDto> updateProduct(@RequestBody ProductWithIdDto productToUpdate) {
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
