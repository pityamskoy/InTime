package team.capybara.backend.spring.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.favorite.FavoriteDto;
import team.capybara.backend.spring.controllers.services.FavoriteService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/favorites")
@CrossOrigin(value = {"http://localhost:3000"})
@SuppressWarnings(value = {"unused"})
public final class FavoriteController {
    private static final Logger log = LoggerFactory.getLogger(FavoriteController.class);

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteDto>> getAllFavorites() {
        log.info("Called getAllFavorites");

        return ResponseEntity.ok(favoriteService.getAllFavorites());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteDto> getFavoriteById(@PathVariable UUID id) {
        log.info("Called getFavoriteById; id={}", id);
        Optional<FavoriteDto> favoriteDtoOptional = favoriteService.getFavoriteById(id);

        return favoriteDtoOptional.map(ResponseEntity::ok).orElseGet
                (() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<FavoriteDto> createFavorite(@RequestBody FavoriteDto favoriteToCreate) {
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
    public ResponseEntity<FavoriteDto> updateFavorite(@RequestBody FavoriteDto favoriteToUpdate) {
        log.info("Called updateFavorite; favoriteToUpdate={}", favoriteToUpdate);

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(favoriteService.updateFavorite(favoriteToUpdate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<FavoriteDto> deleteFavorite(@RequestBody UUID id) {
        log.info("Called deleteFavorite; id={}", id);

        try {
            favoriteService.deleteFavorite(id);
            return ResponseEntity.notFound().build();
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
