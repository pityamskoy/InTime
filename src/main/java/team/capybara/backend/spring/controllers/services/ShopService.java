package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.shop.ShopDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.ShopMapper;
import team.capybara.backend.spring.controllers.repositories.ReviewRepository;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import team.capybara.backend.spring.controllers.services.exceptions.ServiceException;
import team.capybara.backend.spring.entities.Review;
import team.capybara.backend.spring.entities.Shop;

@Service
public final class ShopService {
    private final ShopMapper shopMapper;
    private final ShopRepository shopRepository;
    private final ReviewRepository reviewRepository;

    public ShopService(ShopMapper shopMapper, ShopRepository shopRepository, ReviewRepository reviewRepository) {
        this.shopMapper = shopMapper;
        this.shopRepository = shopRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<ShopDto> getAllShops() {
        List<Shop> shops = shopRepository.findAll();

        return shops.stream().map(shopMapper::getEntity).toList();
    }

    public Optional<Shop> getShopById(UUID id) {
        return shopRepository.findById(id);
    }

    public ShopDto createShop(ShopDto shopToCreate) {
        return shopMapper.postEntity(shopToCreate);
    }
    public Optional<Double> getShopStarsById(UUID id) {
        Optional<Shop> shop = shopRepository.findById(id);

        if (shop.isEmpty()) {
            throw new EntityNotFoundException("Shop not found; id=" + id);
        }

        List<Review> reviews = reviewRepository.findByShop(shop.get());
        double sumOfStarsValue = 0.0;
        double colOfReview = 0.0;

        for (Review review:reviews) {
            sumOfStarsValue+=review.getStars();
            colOfReview++;
        }

        return Optional.of(sumOfStarsValue / colOfReview);
    }

    public ShopDto updateShop(ShopDto shopToUpdate){
        try {
            return shopMapper.putEntity(shopToUpdate);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteShop(UUID id) {
        try {
            shopMapper.deleteEntity(id);
        }  catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}