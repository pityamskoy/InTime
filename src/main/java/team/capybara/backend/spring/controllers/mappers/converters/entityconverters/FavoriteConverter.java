package team.capybara.backend.spring.controllers.mappers.converters.entityconverters;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mappers.converters.EntityIdConverter;
import team.capybara.backend.spring.controllers.repositories.FavoriteRepository;
import team.capybara.backend.spring.entities.Favorite;

import java.util.Optional;
import java.util.UUID;

@Component
public final class FavoriteConverter implements EntityIdConverter<Favorite> {
    private final FavoriteRepository favoriteRepository;

    public FavoriteConverter(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public Favorite toEntity(UUID id) {
        Optional<Favorite> favorite = favoriteRepository.findById(id);

        if (favorite.isEmpty()) {
            throw new EntityNotFoundException("Favorite not found; id=" + id);
        }

        return favorite.get();
    }
}
