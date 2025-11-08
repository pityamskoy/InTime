package team.capybara.backend.spring.controllers.services;

import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.repositories.ImageRepository;
import team.capybara.backend.spring.controllers.repositories.ReviewRepository;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;
import team.capybara.backend.spring.controllers.repositories.UserRepository;
import team.capybara.backend.spring.entities.Image;
import team.capybara.backend.spring.entities.Review;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(
            ReviewRepository reviewRepository
    ) {
        this.reviewRepository = reviewRepository;
    }

    public Review createReview(Review reviewToCreate) {
        Review newReview = new Review(
                reviewToCreate.getId(),
                reviewToCreate.getUser(),
                reviewToCreate.getShop(),
                reviewToCreate.getText(),
                reviewToCreate.getStars()
        );

        return reviewRepository.save(newReview);
    }

    public List<Review> getAllUsers() {
        return reviewRepository.findAll();
    }
}
