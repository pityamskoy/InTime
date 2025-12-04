package team.capybara.backend.spring.controllers.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.capybara.backend.spring.entities.Review;
import team.capybara.backend.spring.entities.Shop;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Page<Review> findByShop(Shop shop, PageRequest pageRequest);
    List<Review> findAllReviewByShop(Shop shop);

    @Query(value = "SELECT AVG(reviews.stars) FROM reviews WHERE reviews.shop_id = :id", nativeQuery = true)
    double calculateStoreRating(@Param("id") UUID id);
}
