package team.capybara.backend.spring.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.image.ImageBase64Dto;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;
import team.capybara.backend.spring.controllers.services.ImageService;
import team.capybara.backend.spring.controllers.services.exceptions.ServiceException;

import java.util.List;
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

        try {
            return ResponseEntity.ok(imageService.getImageById(UUID.fromString(id)));
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/image_load/{id}")
    public ResponseEntity<byte[]> loadImage(@PathVariable String id) {
        log.info("Called loadImage; id={}", id);
        byte[] imageData = null;
        try{
            imageData = imageService.getImageData(id);
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(imageData);
    }

    @PostMapping("/create")
    public ResponseEntity<ImageDto> createImage(@RequestBody ImageDto imageToCreate) {
        log.info("Called createImage; imageToCreate={}", imageToCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.createImage(imageToCreate));
    }

    //fix soon
    @PostMapping("/image_upload/{id}")
    public ResponseEntity<Boolean> uploadImage (@RequestBody ImageBase64Dto imageBase64, @PathVariable String id) {
        log.info("Called uploadImage; imageBase64={}", imageBase64);
        try{
            imageService.saveImageData(imageBase64,id);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CREATED).body(false);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(true);
    }

    @PutMapping("/update")
    public ResponseEntity<ImageDto> updateImage(@RequestBody ImageDto imageToUpdate) {
        log.info("Called updateImage; imageToUpdate={}", imageToUpdate);

        try {
            return ResponseEntity.ok(imageService.updateImage(imageToUpdate));
        } catch (ServiceException e) {
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
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
