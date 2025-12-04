package team.capybara.backend.spring.controllers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.capybara.backend.spring.entities.Favorite;
import team.capybara.backend.spring.entities.ProductType;
import team.capybara.backend.spring.entities.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
    List<Favorite> findFavoritesByUser(User user);
    List<Favorite> findFavoritesByProductType(ProductType productType);
    @Query(value = "SELECT COUNT(*) FROM Favorite WHERE Favorite.product_type_id=:productType AND Favorite.user_id=:user",nativeQuery = true)
    int isProductTypeInFavorites(@Param("user") UUID user,@Param("productType") UUID productType);
}
