package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.entities.review.ReviewDto;
import team.capybara.backend.spring.controllers.dto.entities.review.ReviewWithIdDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.ReviewMapper;
import team.capybara.backend.spring.controllers.repositories.ReviewRepository;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;
import team.capybara.backend.spring.entities.Review;

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

    public Page<ReviewWithIdDto> getAllReviews(int offset, int limit) {
        Page<Review> reviews = reviewRepository.findAll(PageRequest.of(offset, limit));
        return reviews.map(reviewMapper::getEntity);
    }

    public Page<ReviewWithIdDto> getReviewsByShop(UUID id, int offset, int limit) {
        Page<Review> reviews = reviewRepository.findByShop(shopRepository.getById(id), PageRequest.of(offset, limit));
        return reviews.map(reviewMapper::getEntity);
    }

    public Optional<ReviewWithIdDto> getReviewById(UUID id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);

        if (reviewOptional.isPresent()) {
            ReviewWithIdDto reviewWithIdDto = reviewMapper.getEntity(reviewOptional.get());
            return Optional.of(reviewWithIdDto);
        }

        return Optional.empty();
    }

    public ReviewWithIdDto createReview(ReviewDto reviewToCreate) {
        try {
            return reviewMapper.postEntity(reviewToCreate);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public ReviewWithIdDto updateReview(ReviewWithIdDto reviewToUpdate) {
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
