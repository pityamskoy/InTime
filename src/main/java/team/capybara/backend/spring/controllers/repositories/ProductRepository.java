package team.capybara.backend.spring.controllers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.capybara.backend.spring.entitys.Product;

import java.util.UUID;


@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
}
