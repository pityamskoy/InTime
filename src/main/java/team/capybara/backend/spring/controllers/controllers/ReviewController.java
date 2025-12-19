package team.capybara.backend.spring.controllers.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.entities.review.ReviewDto;
import team.capybara.backend.spring.controllers.dto.entities.review.ReviewWithIdDto;
import team.capybara.backend.spring.controllers.dto.other.pagination.PaginationLimit;
import team.capybara.backend.spring.controllers.services.ReviewService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = {"https://vsrok1.bloodstone.boo"}, allowCredentials = "true")
// @CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
@SuppressWarnings(value = {"unused"})
public final class ReviewController {
    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{offset}")
    public ResponseEntity<Page<ReviewWithIdDto>> getAllReviews(
            @PathVariable int offset,
            @RequestBody PaginationLimit limit
    ) {
        log.info("Called getAllReviews; offset={}, limit={}", offset, limit.getLimit());

        return ResponseEntity.ok(reviewService.getAllReviews(offset, limit.getLimit()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewWithIdDto> getReviewById(@PathVariable String id) {
        log.info("Called getReviewById; id={}", id);
        Optional<ReviewWithIdDto> reviewDtoOptional = reviewService.getReviewById(UUID.fromString(id));

        return reviewDtoOptional.map(ResponseEntity::ok).orElseGet
                (() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/get_review_by_shop/{id}/{offset}")
    public ResponseEntity<Page<ReviewWithIdDto>> getReviewsByShop(
            @PathVariable String id,
            @PathVariable int offset,
            @RequestBody PaginationLimit limit
    ) {
        log.info("Called getReviewsByShop; id={}", id);
        return ResponseEntity.ok(reviewService.getReviewsByShop(UUID.fromString(id), offset, limit.getLimit()));
    }

    @PostMapping("/pagination/{id}")
    public ResponseEntity<Integer> getNumberOfPages(@PathVariable String id, @RequestBody PaginationLimit limit) {
        log.info("Called getNumberOfPages; id={}, limit={}", id, limit.getLimit());

        return ResponseEntity.ok(reviewService.getNumberOfShopReviews(UUID.fromString(id), limit.getLimit()));
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
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
