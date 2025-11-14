package team.capybara.backend.spring.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.capybara.backend.spring.FileFromStorageStore;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;
import team.capybara.backend.spring.controllers.services.ImageService;
import team.capybara.backend.spring.exceptions.ImageFlowException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/images")
@CrossOrigin(value = {"http://localhost:3000"})
@SuppressWarnings(value = {"unused"})
public final class ImageController {
    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<List<ImageDto>> getAllImages() {
        log.info("Called getAllImages");

        return ResponseEntity.ok(imageService.getAllImages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDto> getImageById(@PathVariable String id) {
        log.info("Called getImageById; id={}", id);
        Optional<ImageDto> imageDtoOptional = imageService.getImageById(UUID.fromString(id));

        return imageDtoOptional.map(ResponseEntity::ok).orElseGet
                (() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<ImageDto> createImage(@RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        log.info("Called createImage; imageToCreate={}", image.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.createImage(image.getBytes()));
    }

    @PostMapping("/image_upload1/{id}")
    public String handle(@RequestPart(value = "image", required = false) MultipartFile image,@PathVariable String id) throws IOException {

        FileFromStorageStore fs = new FileFromStorageStore();
        fs.saveFile("./src/main/resources/static/",image.getName()+id+".jpg",image.getBytes());

        return image.getName();
    }

    @PutMapping("/update")
    public ResponseEntity<ImageDto> updateImage(@RequestBody ImageDto imageToUpdate) {
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
