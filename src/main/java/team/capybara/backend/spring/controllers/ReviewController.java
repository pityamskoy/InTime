package team.capybara.backend.spring.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.review.ReviewDto;
import team.capybara.backend.spring.controllers.dto.review.ReviewWithIdDto;
import team.capybara.backend.spring.controllers.services.ReviewService;

import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<List<ReviewWithIdDto>> getAllReviews() {
        log.info("Called getAllReviews");

        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewWithIdDto> getReviewById(@PathVariable String id) {
        log.info("Called getReviewById; id={}", id);
        Optional<ReviewWithIdDto> reviewDtoOptional = reviewService.getReviewById(UUID.fromString(id));

        return reviewDtoOptional.map(ResponseEntity::ok).orElseGet
                (() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/get_review_by_shop/{id}")
    public ResponseEntity<List<ReviewWithIdDto>> getReviewsByShop(@PathVariable String id) {
        log.info("Called getReviewsByShop; id={}", id);
        return ResponseEntity.ok(reviewService.getReviewsByShop(UUID.fromString(id)));
    }

    @PostMapping("/create")
    public ResponseEntity<ReviewWithIdDto> createReview(@RequestBody ReviewDto reviewToCreate) {
        log.info("Called createReview, reviewToCreate={}", reviewToCreate);

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(reviewToCreate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ReviewWithIdDto> updateReview(@RequestBody ReviewWithIdDto reviewToUpdate) {
        log.info("Called updateReview, reviewToUpdate={}", reviewToUpdate);

        try {
            return ResponseEntity.ok(reviewService.updateReview(reviewToUpdate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteReview(@RequestBody String id) {
        log.info("Called deleteReview, id={}", id);

        try {
            reviewService.deleteReview(UUID.fromString(id));
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
