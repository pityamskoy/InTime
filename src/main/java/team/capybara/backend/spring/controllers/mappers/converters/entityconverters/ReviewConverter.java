package team.capybara.backend.spring.controllers.mappers.converters.entityconverters;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mappers.converters.EntityIdConverter;
import team.capybara.backend.spring.controllers.repositories.ReviewRepository;
import team.capybara.backend.spring.entities.Review;

import java.util.Optional;
import java.util.UUID;

@Component
public final class ReviewConverter implements EntityIdConverter<Review> {
    private final ReviewRepository reviewRepository;

    public ReviewConverter(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review toEntity(UUID id) {
        Optional<Review> review = reviewRepository.findById(id);

        if (review.isEmpty()) {
            throw new EntityNotFoundException("Review not found; id=" + id);
        }

        return review.get();
    }
}
