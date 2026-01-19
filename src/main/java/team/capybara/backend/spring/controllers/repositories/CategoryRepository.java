package team.capybara.backend.spring.controllers.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.tokens.Token;
import team.capybara.backend.spring.entities.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @EntityGraph(attributePaths = {"productTypes"})
    @Override
    List<Category> findAll();

    @EntityGraph(attributePaths = {"productTypes"})
    @Override
    Optional<Category> findById(UUID id);
}
