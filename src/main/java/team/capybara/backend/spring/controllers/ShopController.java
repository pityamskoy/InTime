package team.capybara.backend.spring.controllers;


import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.product.ProductDto;
import team.capybara.backend.spring.controllers.services.ShopService;
import team.capybara.backend.spring.entitys.Product;

import java.util.NoSuchElementException;

@RestController
@CrossOrigin(value = {"http://localhost:3000"})
@RequestMapping("/shops")
@SuppressWarnings(value = {"unused"})
public class ShopController {
    private static final Logger log = LoggerFactory.getLogger(ShopController.class);
    ShopService shopService;

    ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productToCreate) {
        log.info("Called createProduct product={}", productToCreate);

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(shopService.createProduct(productToCreate));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<Product> updateProduct(
            @RequestBody ProductDto productToUpdate
    ) {
        log.info("Called updateProduct id={}, productToUpdate={}", productToUpdate.id(), productToUpdate);

        try {
            Product updated = shopService.updateProduct(productToUpdate);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //fix
    @PatchMapping("/product/{id}")
    public ResponseEntity<Product> partiallyUpdateProduct() {
        log.info("Called partiallyUpdateProduct");
        return null;
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id) {
        log.info("Called deleteProduct id={}", id);

        try {
            shopService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
