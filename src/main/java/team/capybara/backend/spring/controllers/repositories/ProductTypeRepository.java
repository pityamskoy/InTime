package team.capybara.backend.spring.controllers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team.capybara.backend.spring.entities.Product;
import team.capybara.backend.spring.entities.ProductType;

import java.util.List;
import java.util.UUID;

public interface ProductTypeRepository extends JpaRepository<ProductType, UUID> {
    List<ProductType> findByNameContaining(String name);
}
