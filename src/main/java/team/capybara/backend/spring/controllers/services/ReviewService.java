package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.review.ReviewDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.ReviewMapper;
import team.capybara.backend.spring.controllers.repositories.ReviewRepository;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;
import team.capybara.backend.spring.entities.Review;
import team.capybara.backend.spring.entities.Shop;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public final class ReviewService {
    private final ReviewMapper reviewMapper;
    private final ReviewRepository reviewRepository;
    private final ShopRepository shopRepository;

    public ReviewService(
            ReviewMapper reviewMapper,
            ReviewRepository reviewRepository, ShopRepository shopRepository
    ) {
        this.reviewMapper = reviewMapper;
        this.reviewRepository = reviewRepository;
        this.shopRepository = shopRepository;
    }

    public List<ReviewDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();

        return reviews.stream().map(reviewMapper::getEntity).toList();
    }

    public List<ReviewDto> getReviewsByShop(UUID id) {
        List<Review> reviews = reviewRepository.findByShop(shopRepository.getById(id));
        return reviews.stream().map(reviewMapper::getEntity).toList();
    }

    public Optional<ReviewDto> getReviewById(UUID id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);

        if (reviewOptional.isPresent()) {
            ReviewDto reviewDto = reviewMapper.getEntity(reviewOptional.get());
            return Optional.of(reviewDto);
        }

        return Optional.empty();
    }

    public ReviewDto createReview(ReviewDto reviewToCreate) {
        try {
            return reviewMapper.postEntity(reviewToCreate);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public ReviewDto updateReview(ReviewDto reviewToUpdate) {
        try {
            return reviewMapper.putEntity(reviewToUpdate);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public void deleteReview(UUID id) {
        try {
            reviewRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
