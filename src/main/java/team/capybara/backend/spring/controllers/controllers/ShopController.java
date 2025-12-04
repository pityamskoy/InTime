package team.capybara.backend.spring.controllers.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.entities.shop.ShopDto;
import team.capybara.backend.spring.controllers.dto.entities.shop.ShopWithIdDto;
import team.capybara.backend.spring.controllers.services.ShopService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/shops")
@SuppressWarnings(value = {"unused"})
public final class ShopController {
    private static final Logger log = LoggerFactory.getLogger(ShopController.class);

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping(value = {"", "/{userLat}/{userLon}"})
    public ResponseEntity<List<ShopWithIdDto>> getAllShops(@PathVariable Map<String, String> params) {
        log.info("Called getAllShops");
        ResponseEntity<List<ShopWithIdDto>> response;

        if (params.isEmpty())
            response = ResponseEntity.ok(shopService.getAllShops());
        else {
            try {
                return ResponseEntity.ok(shopService.getAllShops(Double.parseDouble(params.get("userLat")), Double.parseDouble(params.get("userLon"))));
            } catch (Exception e) {
                response = ResponseEntity.badRequest().build();
            }
        }

        return response;
    }

    @GetMapping(value = {"/{id}", "/{id}/{userLat}/{userLon}"})
    public ResponseEntity<ShopWithIdDto> getShopById(@PathVariable Map<String, String> params) {
        log.info("Called getShopById; id={}", params.get("id"));

        Optional<ShopWithIdDto> shopDtoOptional = Optional.empty();

        if(params.size()==1)
            shopDtoOptional = shopService.getShopById(UUID.fromString(params.get("id")));
        else if(params.size()==3){
            try{
                shopDtoOptional  = shopService.getShopById(UUID.fromString(params.get("id")),Double.parseDouble(params.get("userLat")),Double.parseDouble(params.get("userLon")));
            }
            catch (Exception e){
                shopDtoOptional = Optional.empty();
            }
        }

        return shopDtoOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/shop_stars/{id}")
    public ResponseEntity<Double> getShopStarsById(@PathVariable String id) {
        log.info("Called getShopStarsById; id={}", id);
        Optional<Double> shopOptional = shopService.getShopStarsById(UUID.fromString(id));

        return ResponseEntity.status(HttpStatus.CREATED).body(shopOptional.get());
    }

    @PostMapping("/create")
    public ResponseEntity<ShopWithIdDto> createShop(@RequestBody ShopDto shopToCreate) {
        log.info("Called createShop; shopToCreate={}", shopToCreate);

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(shopService.createShop(shopToCreate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ShopWithIdDto> updateShop(@RequestBody ShopWithIdDto shopToUpdate) {
        log.info("Called updateShop; shopToUpdate={}", shopToUpdate);

        try {
            return ResponseEntity.ok(shopService.updateShop(shopToUpdate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
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
