package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.shop.ShopDto;
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

    public List<ShopDto> getAllShops() {
        List<Shop> shops = shopRepository.findAll();
        List<ShopDto> shopDtos = shops.stream().map(shopMapper::getEntity).toList();
        return shopDtos;
    }

    public List<ShopDto> getAllShops(double lat,double lon) {
        List<Shop> shops = shopRepository.findAll();
        shops.stream().forEach(shop -> shop.getDistanceTo(lat,lon));
        List<ShopDto> shopDtos = shops.stream().map(shopMapper::getEntity).toList();
        return shopDtos;
    }

    public Optional<ShopDto> getShopById(UUID id) {
        Optional<Shop> shopOptional = shopRepository.findById(id);

        if  (shopOptional.isPresent()) {
            ShopDto shopDto = shopMapper.getEntity(shopOptional.get());
            return Optional.of(shopDto);
        }

        return Optional.empty();
    }

    public Optional<ShopDto> getShopById(UUID id, double lat, double lon) {
        Optional<Shop> shopOptional = shopRepository.findById(id);

        if  (shopOptional.isPresent()) {
            Shop shop = shopOptional.get();
            shop.getDistanceTo(lat,lon);
            ShopDto shopDto = shopMapper.getEntity(shop);
            return Optional.of(shopDto);
        }

        return Optional.empty();
    }

    public List<ShopDto> sortShopsByDistance(List<UUID> shopsId, Double distance) {
        List<ShopDto> shopsWithAppropriateDistance = new ArrayList<>();

        for (UUID shopId : shopsId) {
            Optional<ShopDto> shopDtoOptional = getShopById(shopId);
            if (shopDtoOptional.isPresent()) {
                ShopDto shopDto = shopDtoOptional.get();
                if (shopDto.distance() <= distance) {
                    shopsWithAppropriateDistance.add(shopDto);
                }
            }
        }

        return shopsWithAppropriateDistance;
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