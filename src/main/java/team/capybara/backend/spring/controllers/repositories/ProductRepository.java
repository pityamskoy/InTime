package team.capybara.backend.spring.controllers.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.capybara.backend.spring.entities.Product;
import team.capybara.backend.spring.entities.ProductType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @EntityGraph(attributePaths = {"productType"})
    @Override
    Page<Product> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"productType"})
    @Override
    Optional<Product> findById(UUID id);

    @EntityGraph(attributePaths = {"productType"})
    List<Product> findByProductType(ProductType productType);

}
