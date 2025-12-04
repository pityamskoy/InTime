package team.capybara.backend.spring.controllers.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.entities.favorite.FavoriteWithIdDto;
import team.capybara.backend.spring.controllers.dto.entities.favorite.FavoriteDto;
import team.capybara.backend.spring.controllers.services.FavoriteService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/favorites")
@SuppressWarnings(value = {"unused"})
public final class FavoriteController {
    private static final Logger log = LoggerFactory.getLogger(FavoriteController.class);

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteWithIdDto>> getAllFavorites() {
        log.info("Called getAllFavorites");

        return ResponseEntity.ok(favoriteService.getAllFavorites());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteWithIdDto> getFavoriteById(@PathVariable String id) {
        log.info("Called getFavoriteById; id={}", id);
        Optional<FavoriteWithIdDto> favoriteDtoOptional = favoriteService.getFavoriteById(UUID.fromString(id));

        return favoriteDtoOptional.map(ResponseEntity::ok).orElseGet
                (() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{user_id}/{product_type_id}")
    public ResponseEntity<Boolean> isFavorite(@PathVariable String user_id,@PathVariable String product_type_id) {
        log.info("Called isFavorite; user_id={}, product_type_id={}", user_id,product_type_id);
        Optional<Boolean> isFavorite = favoriteService.isFavorite(UUID.fromString(product_type_id),UUID.fromString(user_id));

        return isFavorite.map(ResponseEntity::ok).orElseGet
                (() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<FavoriteWithIdDto>> getFavoriteByUserId(@PathVariable String id) {
        log.info("Called getFavoriteByUserId; id={}", id);

        try {
            return ResponseEntity.ok(favoriteService.getFavoritesByUserId(UUID.fromString(id)));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<FavoriteWithIdDto> createFavorite(@RequestBody FavoriteDto favoriteToCreate) {
        log.info("Called createFavorite; favoriteToCreate={}", favoriteToCreate);

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(favoriteService.createFavorite(favoriteToCreate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<FavoriteWithIdDto> updateFavorite(@RequestBody FavoriteWithIdDto favoriteToUpdate) {
        log.info("Called updateFavorite; favoriteToUpdate={}", favoriteToUpdate);

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(favoriteService.updateFavorite(favoriteToUpdate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable String id) {
        log.info("Called deleteFavorite; id={}", id);

        try {
            favoriteService.deleteFavorite(UUID.fromString(id));
            return ResponseEntity.notFound().build();
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
