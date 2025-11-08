package team.capybara.backend.spring.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.review.ReviewDto;
import team.capybara.backend.spring.controllers.services.ReviewService;
import team.capybara.backend.spring.controllers.services.exceptions.ServiceException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(value = {"http://localhost:3000"})
@SuppressWarnings(value = {"unused"})
public final class ReviewController {
    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        log.info("Called getAllReviews");

        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @PostMapping("/create")
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto reviewToCreate) {
        log.info("Called createReview, reviewToCreate={}", reviewToCreate);

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(reviewToCreate));
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ReviewDto> updateReview(@RequestBody ReviewDto reviewToUpdate) {
        log.info("Called updateReview, reviewToUpdate={}", reviewToUpdate);

        try {
            return ResponseEntity.ok(reviewService.updateReview(reviewToUpdate));
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ReviewDto> deleteReview(@RequestBody String id) {
        log.info("Called deleteReview, id={}", id);

        try {
            reviewService.deleteReview(UUID.fromString(id));
            return ResponseEntity.noContent().build();
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
