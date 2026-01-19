package team.capybara.backend.spring.entities;

import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.repositories.ReviewRepository;

/**
 * {@code EntityHandler} is the class for connection between services and entities.
 */
@Component
public final class EntityHandler {
    private final ReviewRepository reviewRepository;

    public EntityHandler(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Double calculateProductScore(Product product, Double userLat, Double userLon) {
        try {
            Double rating = reviewRepository.calculateStoreRating(product.getProductType().getShop().getId());
            product.calculateScore(userLat, userLon, rating);
        } catch (Exception e) {
            Double rating = 5.0;
            product.calculateScore(userLat, userLon, rating);
        }

        return product.getScore();
    }
}
