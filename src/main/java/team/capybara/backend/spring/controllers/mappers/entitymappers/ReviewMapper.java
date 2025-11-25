package team.capybara.backend.spring.controllers.mappers.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.dto.review.ReviewDto;
import team.capybara.backend.spring.controllers.dto.review.ReviewWithIdDto;
import team.capybara.backend.spring.controllers.mappers.Mapper;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ReviewConverter;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ShopConverter;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.UserConverter;
import team.capybara.backend.spring.controllers.repositories.ReviewRepository;
import team.capybara.backend.spring.entities.Review;
import team.capybara.backend.spring.entities.Shop;
import team.capybara.backend.spring.entities.User;

import java.util.UUID;

@Component
public final class ReviewMapper implements Mapper<Review, ReviewWithIdDto, ReviewDto> {
    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;
    private final UserConverter userConverter;
    private final ShopConverter shopConverter;

    public ReviewMapper(
            ReviewRepository reviewRepository,
            ReviewConverter reviewConverter,
            UserConverter userConverter,
            ShopConverter shopConverter
    ) {
        this.reviewRepository = reviewRepository;
        this.reviewConverter = reviewConverter;
        this.userConverter = userConverter;
        this.shopConverter = shopConverter;
    }

    @Override
    public ReviewWithIdDto getEntity(Review review) {
        return new ReviewWithIdDto(
                review.getId(),
                review.getUser().getId(),
                review.getShop().getId(),
                review.getText(),
                review.getStars()
        );
    }

    @Override
    public ReviewWithIdDto postEntity(ReviewDto reviewToCreate) {
        try {
            User user = userConverter.toEntity(reviewToCreate.userId());
            Shop shop = shopConverter.toEntity(reviewToCreate.shopId());

            Review reviewCreated = reviewRepository.save(new Review(
                    UUID.randomUUID(),
                    user,
                    shop,
                    reviewToCreate.text(),
                    reviewToCreate.stars()
            ));

            return getEntity(reviewCreated);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public ReviewWithIdDto putEntity(ReviewWithIdDto reviewToUpdate) {
        try {
            Review reviewUpdated = reviewConverter.toEntity(reviewToUpdate.id());
            User user = userConverter.toEntity(reviewToUpdate.userId());
            Shop shop = shopConverter.toEntity(reviewToUpdate.shopId());

            reviewUpdated.setUser(user);
            reviewUpdated.setShop(shop);
            reviewUpdated.setText(reviewToUpdate.text());
            reviewUpdated.setStars(reviewToUpdate.stars());
            reviewRepository.save(reviewUpdated);

            return getEntity(reviewUpdated);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }

    }

    @Override
    public void deleteEntity(UUID entityId) {
        try {
            Review reviewDeleted = reviewConverter.toEntity(entityId);
            reviewRepository.delete(reviewDeleted);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
