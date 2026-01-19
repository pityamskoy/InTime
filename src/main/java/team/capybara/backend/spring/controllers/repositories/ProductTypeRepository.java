package team.capybara.backend.spring.controllers.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import team.capybara.backend.spring.entities.ProductType;
import team.capybara.backend.spring.entities.Shop;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductTypeRepository extends JpaRepository<ProductType, UUID> {

    @EntityGraph(attributePaths = {"mainImage","images","shop"})
    @Override
    List<ProductType> findAll();

    @EntityGraph(attributePaths = {"mainImage","images","shop"})
    @Override
    Optional<ProductType> findById(UUID id);

    @EntityGraph(attributePaths = {"mainImage","images","shop"})
    List<ProductType> findByNameContainingIgnoreCase(String name);

    @EntityGraph(attributePaths = {"mainImage","images","shop"})
    List<ProductType> findByShop(Shop shop);
}
