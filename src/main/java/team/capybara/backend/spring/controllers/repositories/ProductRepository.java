package team.capybara.backend.spring.controllers.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.capybara.backend.spring.entities.Product;
import team.capybara.backend.spring.entities.ProductType;

import java.util.List;
import java.util.UUID;


@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByProductType(ProductType productType );
    Page<Product> findByProductTypeIn(List<ProductType> productTypes, Pageable pageable);
    List<Product> findByProductType_Shop_Id(UUID shopId);
}
