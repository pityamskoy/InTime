package team.capybara.backend.spring.controllers;

import jakarta.servlet.MultipartConfigElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import team.capybara.backend.spring.FileFromStorageStore;
import team.capybara.backend.spring.controllers.dto.image.ImageBase64Dto;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;
import team.capybara.backend.spring.controllers.services.ImageService;
import team.capybara.backend.spring.controllers.services.ServiceException;
import team.capybara.backend.spring.entities.Image;

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

    @GetMapping("image_load/{id}")
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
    public ResponseEntity<Image> createImage(@RequestBody ImageDto imageToCreate) {
        log.info("Called createImage; imageToCreate={}", imageToCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.createImage(imageToCreate));
    }

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

    public ResponseEntity<Image> updateImage(@RequestBody ImageDto imageToUpdate) {
        log.info("Called updateImage; imageToUpdate={}", imageToUpdate);

        try {
            return ResponseEntity.ok(imageService.updateImage(imageToUpdate));
        } catch (ServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteImage(@RequestBody String id) {
        log.info("Called deleteImage; id={}", id);

        try {
            imageService.deleteImage(UUID.fromString(id));
            return ResponseEntity.noContent().build();
        } catch (ServiceException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
