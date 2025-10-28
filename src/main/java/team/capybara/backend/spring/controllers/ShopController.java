package team.capybara.backend.spring.controllers;


import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.product.ProductDto;
import team.capybara.backend.spring.controllers.dto.shop.ShopDto;
import team.capybara.backend.spring.controllers.services.ShopService;
import team.capybara.backend.spring.entitys.Product;
import team.capybara.backend.spring.entitys.Shop;

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
    public ResponseEntity<Shop> createProduct(@RequestBody ShopDto shopToCreate) {
        log.info("Called createShop product={}", shopToCreate);

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(shopService.createShop(shopToCreate));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/shop/{id}")
    public ResponseEntity<Shop> updateProduct(
            @RequestBody ShopDto shopToUpdate
    ) {
        log.info("Called updateShop id={}, shopToUpdate={}", shopToUpdate.id(), shopToUpdate);

        try {
            Shop updated = shopService.updateShop(shopToUpdate);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //fix
    @PatchMapping("/shop/{id}")
    public ResponseEntity<Product> partiallyUpdateProduct() {
        log.info("Called partiallyUpdateShop");
        return null;
    }

    @DeleteMapping("/shop/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id) {
        log.info("Called deleteShop id={}", id);

        try {
            shopService.deleteShop(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
