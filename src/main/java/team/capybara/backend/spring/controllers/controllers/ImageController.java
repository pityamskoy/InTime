package team.capybara.backend.spring.controllers.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.capybara.backend.spring.controllers.dto.entities.image.ImageWithIdDto;
import team.capybara.backend.spring.controllers.services.ImageService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(value = {"https://image.bloodstone.boo:443"})
@RestController
@RequestMapping("/images")
@SuppressWarnings(value = {"unused"})
public final class ImageController {
    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<List<ImageWithIdDto>> getAllImages() {
        log.info("Called getAllImages");

        return ResponseEntity.ok(imageService.getAllImages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageWithIdDto> getImageById(@PathVariable String id) {
        log.info("Called getImageById; id={}", id);
        Optional<ImageWithIdDto> imageDtoOptional = imageService.getImageById(UUID.fromString(id));

        return imageDtoOptional.map(ResponseEntity::ok).orElseGet
                (() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<ImageWithIdDto> createImage(@RequestPart(value = "image", required = false) MultipartFile image) throws Exception {
        log.info("Called createImage; imageToCreate={}", image.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.createImage(image.getBytes()));
    }

    @PutMapping("/update")
    public ResponseEntity<ImageWithIdDto> updateImage(@RequestBody ImageWithIdDto imageToUpdate) {
        log.info("Called updateImage; imageToUpdate={}", imageToUpdate);

        try {
            return ResponseEntity.ok(imageService.updateImage(imageToUpdate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteImage(@RequestBody String id) {
        log.info("Called deleteImage; id={}", id);

        try {
            imageService.deleteImage(UUID.fromString(id));
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
