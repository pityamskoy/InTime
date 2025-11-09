package team.capybara.backend.spring.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.shop.ShopDto;
import team.capybara.backend.spring.controllers.services.ShopService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/shops")
@CrossOrigin(value = {"http://localhost:3000"})
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

        return ResponseEntity.ok(shopService.getAllShops());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShopDto> getShopById(@PathVariable String id) {
        log.info("Called getShopById; id={}", id);
        Optional<ShopDto> shopDtoOptional = shopService.getShopById(UUID.fromString(id));

        return shopDtoOptional.map(ResponseEntity::ok).orElseGet
                (() -> ResponseEntity.notFound().build());
    }

    //fix soon
    @GetMapping("/shop_stars/{id}")
    public ResponseEntity<Double> getShopStarsById(@PathVariable String id) {
        log.info("Called getShopStarsById; id={}", id);
        Optional<Double> shopOptional = shopService.getShopStarsById(UUID.fromString(id));

        return ResponseEntity.status(HttpStatus.CREATED).body(shopOptional.get());
    }

    @PostMapping("/create")
    public ResponseEntity<ShopDto> createShop(@RequestBody ShopDto shopToCreate) {
        log.info("Called createShop; shopToCreate={}", shopToCreate);

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(shopService.createShop(shopToCreate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ShopDto> updateShop(@RequestBody ShopDto shopToUpdate) {
        log.info("Called updateShop; shopToUpdate={}", shopToUpdate);

        try {
            return ResponseEntity.ok(shopService.updateShop(shopToUpdate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteProduct(@RequestBody String id) {
        log.info("Called deleteShop; id={}", id);

        try {
            shopService.deleteShop(UUID.fromString(id));
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
