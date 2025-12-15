package team.capybara.backend.spring.controllers.controllers;

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
import team.capybara.backend.spring.controllers.services.FeedService;
import team.capybara.backend.spring.entities.*;
import team.capybara.backend.spring.controllers.dto.entities.product.ProductWithIdDto;

import java.util.*;

@RestController
@RequestMapping("/feed")
@CrossOrigin(origins = {"https://vsrok.bloodstone.boo:443/"}, allowCredentials = "true")
// @CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
@SuppressWarnings(value = {"unused"})
public final class FeedController{
    private static final Logger log = LoggerFactory.getLogger(FeedController.class);

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @PostMapping("/{offset}")
    public ResponseEntity<Page<ProductWithIdDto>> getAllProducts(
            @PathVariable int offset,
            @RequestBody FeedFilterEntity filter
    ) {
        log.info("Called getAllSortedProducts; offset={}; filter={}", offset, filter);

        if (offset < 1) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(feedService.getProducts(offset, filter));
    }

    @PostMapping("/products_by_shop/{id}/{offset}")
    public ResponseEntity<Page<ProductWithIdDto>> getProductsByShopId(
            @PathVariable String id,
            @PathVariable int offset,
            @RequestBody PaginationLimit limit
    ) {
        log.info("Called getProductsByShopId; id={}, offset={}, limit={}", id, offset, limit.getLimit());

        if (offset < 1) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(feedService.getProductsByShop(offset, limit.getLimit(), id));
    }

    @PostMapping("/pagination")
    public ResponseEntity<Integer> getNumberOfPages(@RequestBody FeedFilterEntity filter) {
        log.info("Called getNumberOfOffsets; filter={}", filter);

        return ResponseEntity.ok(feedService.getNumberOfPages(filter));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductWithIdDto> getProductById(@PathVariable String id) {
        log.info("Called getProduct; id={}", id);
        Optional<ProductWithIdDto> productDtoOptional = feedService.getProductById(UUID.fromString(id));

        return productDtoOptional.map(ResponseEntity::ok).orElseGet
                (() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<ProductWithIdDto> createProduct(@RequestBody ProductDto productToCreate) {
        log.info("Called createProduct; productToCreate={}", productToCreate);

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(feedService.createProduct(productToCreate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ProductWithIdDto> updateProduct(@RequestBody ProductWithIdDto productToUpdate) {
        log.info("Called updateProduct; productToUpdate={}", productToUpdate);

        try {
            return ResponseEntity.ok(feedService.updateProduct(productToUpdate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        log.info("Called deleteProduct; id={}", id);

        try {
            feedService.deleteProduct(UUID.fromString(id));
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
