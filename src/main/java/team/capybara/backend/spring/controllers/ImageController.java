package team.capybara.backend.spring.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;
import team.capybara.backend.spring.controllers.services.ImageService;
import team.capybara.backend.spring.entities.Image;

import java.util.List;

@RestController
@CrossOrigin(value = {"http://localhost:3000"})
@RequestMapping("/images")
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

    @PostMapping("/create")
    public ResponseEntity<Image> createImage(@RequestBody ImageDto imageDto) {
        log.info("Called createImage");

        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.createImage(imageDto));
    }
}
