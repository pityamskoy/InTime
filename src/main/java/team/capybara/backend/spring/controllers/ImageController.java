package team.capybara.backend.spring.controllers;

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
    ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<List<Image>> getAllImages() {
        return ResponseEntity.ok(imageService.getAllImages());
    }

    @PostMapping("/create")
    public ResponseEntity<Image> createImage(@RequestBody ImageDto imageDto) {
        return ResponseEntity.ok(imageService.createImage(imageDto));
    }
}
