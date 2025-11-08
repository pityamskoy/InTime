package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.review.ReviewDto;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ReviewConverter;
import team.capybara.backend.spring.controllers.mappers.entitymappers.ReviewMapper;
import team.capybara.backend.spring.controllers.repositories.ImageRepository;
import team.capybara.backend.spring.controllers.repositories.ReviewRepository;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;
import team.capybara.backend.spring.controllers.repositories.UserRepository;
import team.capybara.backend.spring.controllers.services.exceptions.ServiceException;
import team.capybara.backend.spring.entities.Image;
import team.capybara.backend.spring.entities.Review;

import java.util.List;
import java.util.UUID;

@Service
public final class ReviewService {
    private final ReviewMapper reviewMapper;
    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;

    public ReviewService(
            ReviewMapper reviewMapper,
            ReviewRepository reviewRepository,
            ReviewConverter reviewConverter) {
        this.reviewMapper = reviewMapper;
        this.reviewRepository = reviewRepository;
        this.reviewConverter = reviewConverter;
    }

    public List<ReviewDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();

        return reviews.stream().map(reviewMapper::getEntity).toList();
    }

    public ReviewDto getReviewById(UUID id) {
        try {
            return reviewMapper.getEntity(reviewConverter.toEntity(id));
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public ReviewDto createReview(ReviewDto reviewToCreate) {
        try {
            return reviewMapper.postEntity(reviewToCreate);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public ReviewDto updateReview(ReviewDto reviewToUpdate) {
        try {
            return reviewMapper.putEntity(reviewToUpdate);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteReview(UUID id) {
        try {
            reviewRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
