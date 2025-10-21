package team.capybara.backend.spring.controllers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.capybara.backend.hibernate.ProductType;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, String> {
}
