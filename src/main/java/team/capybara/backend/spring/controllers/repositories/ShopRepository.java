package team.capybara.backend.spring.controllers.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.capybara.backend.spring.entities.Shop;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShopRepository extends JpaRepository<Shop, UUID> {
    @EntityGraph(attributePaths = {"mainImage", "images","owner"})
    @Override
    List<Shop> findAll();

    @EntityGraph(attributePaths = {"mainImage", "images","owner"})
    @Override
    Optional<Shop> findById(UUID id);
}
