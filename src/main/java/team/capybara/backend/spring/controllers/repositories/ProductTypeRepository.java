package team.capybara.backend.spring.controllers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team.capybara.backend.hibernate.ProductType;

public interface ProductTypeRepository extends JpaRepository<ProductType, String> {
}
