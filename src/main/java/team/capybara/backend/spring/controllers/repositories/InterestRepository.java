package team.capybara.backend.spring.controllers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.capybara.backend.spring.entities.Interest;
import team.capybara.backend.spring.entities.Product;
import team.capybara.backend.spring.entities.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface InterestRepository extends JpaRepository<Interest, UUID> {
    List<Interest> findByUser(User user );
    List<Interest> findByProduct(Product product );
    @Query(value = "SELECT COUNT(*) FROM Interests WHERE Interests.product_id=:product AND Interests.user_id=:user",nativeQuery = true)
    int isProductInInterests(@Param("user") UUID user, @Param("product") UUID product);
    @Query(value = "SELECT COUNT(*) FROM Interests WHERE Interests.product_id=:product",nativeQuery = true)
    int colProductInInterests( @Param("product") UUID product);
}
