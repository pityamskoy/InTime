package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.shop.ShopDto;
import team.capybara.backend.spring.controllers.dto.shop.ShopWithIdDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.ShopMapper;
import team.capybara.backend.spring.controllers.repositories.ReviewRepository;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public List<ShopWithIdDto> getAllShops() {
        List<Shop> shops = shopRepository.findAll();
        List<ShopWithIdDto> shopWithIdDtos = shops.stream().map(shopMapper::getEntity).toList();
        return shopWithIdDtos;
    }

    public List<ShopWithIdDto> getAllShops(double lat, double lon) {
        List<Shop> shops = shopRepository.findAll();
        shops.stream().forEach(shop -> shop.getDistanceTo(lat,lon));
        List<ShopWithIdDto> shopWithIdDtos = shops.stream().map(shopMapper::getEntity).toList();
        return shopWithIdDtos;
    }

    public Optional<ShopWithIdDto> getShopById(UUID id) {
        Optional<Shop> shopOptional = shopRepository.findById(id);

        if  (shopOptional.isPresent()) {
            ShopWithIdDto shopWithIdDto = shopMapper.getEntity(shopOptional.get());
            return Optional.of(shopWithIdDto);
        }

        return Optional.empty();
    }

    public Optional<ShopWithIdDto> getShopById(UUID id, double lat, double lon) {
        Optional<Shop> shopOptional = shopRepository.findById(id);

        if  (shopOptional.isPresent()) {
            Shop shop = shopOptional.get();
            shop.getDistanceTo(lat,lon);
            ShopWithIdDto shopWithIdDto = shopMapper.getEntity(shop);
            return Optional.of(shopWithIdDto);
        }

        return Optional.empty();
    }

    public List<ShopWithIdDto> sortShopsByDistance(List<UUID> shopsId, Double distance) {
        List<ShopWithIdDto> shopsWithAppropriateDistance = new ArrayList<>();

        for (UUID shopId : shopsId) {
            Optional<ShopWithIdDto> shopDtoOptional = getShopById(shopId);
            if (shopDtoOptional.isPresent()) {
                ShopWithIdDto shopWithIdDto = shopDtoOptional.get();
                if (shopWithIdDto.distance() <= distance) {
                    shopsWithAppropriateDistance.add(shopWithIdDto);
                }
            }
        }

        return shopsWithAppropriateDistance;
    }

    public ShopWithIdDto createShop(ShopDto shopToCreate) {
        try {
            return shopMapper.postEntity(shopToCreate);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
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

    public ShopWithIdDto updateShop(ShopWithIdDto shopToUpdate){
        try {
            return shopMapper.putEntity(shopToUpdate);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public void deleteShop(UUID id) {
        try {
            shopMapper.deleteEntity(id);
        }  catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}