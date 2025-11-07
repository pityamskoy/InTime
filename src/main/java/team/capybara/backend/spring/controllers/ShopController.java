package team.capybara.backend.spring.controllers;


import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.shop.ShopDto;
import team.capybara.backend.spring.controllers.services.ShopService;
import team.capybara.backend.spring.entities.Shop;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(value = {"http://localhost:3000"})
@RequestMapping("/shops")
@SuppressWarnings(value = {"unused"})
public final class ShopController {
    private static final Logger log = LoggerFactory.getLogger(ShopController.class);
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping
    public ResponseEntity<List<ShopDto>> getAllShops() {
        log.info("Called getAllShops");

        return ResponseEntity.status(HttpStatus.OK).body(shopService.getAllShops());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShopDto> getShopById(@PathVariable String id) {
        log.info("Called getShopById id={}", id);

        Optional<ShopDto> shopDto = shopService.getShopById(UUID.fromString(id));

        if (shopDto.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(shopDto.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Shop> createShop(@RequestBody ShopDto shopToCreate) {
        log.info("Called createShop product={}", shopToCreate);

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(shopService.createShop(shopToCreate));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Shop> updateShop(@RequestBody ShopDto shopToUpdate) {
        log.info("Called updateShop id={}, shopToUpdate={}", shopToUpdate.id(), shopToUpdate);

        try {
            Shop updated = shopService.updateShop(shopToUpdate);
            return ResponseEntity.status(HttpStatus.OK).body(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id) {
        log.info("Called deleteShop id={}", id);

        try {
            shopService.deleteShop(UUID.fromString(id));
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
