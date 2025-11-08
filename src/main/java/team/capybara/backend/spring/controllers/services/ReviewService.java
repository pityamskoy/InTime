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
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final ImageRepository imageRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            UserRepository userRepository,
            ShopRepository shopRepository,
            ImageRepository imageRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.shopRepository = shopRepository;
        this.imageRepository = imageRepository;
    }

    public Review createReview(Review reviewToCreate) {
        Review newReview = new Review(
                reviewToCreate.getId(),
                reviewToCreate.getUser(),
                reviewToCreate.getShop(),
                reviewToCreate.getText(),
                reviewToCreate.getStars()
        );

        for(Image img:newReview.getShop().getImages())
            imageRepository.save(img);

        userRepository.save(newReview.getUser());
        shopRepository.save(newReview.getShop());

        return reviewRepository.save(newReview);
    }

    public List<Review> getAllUsers() {
        return reviewRepository.findAll();
    }
}
