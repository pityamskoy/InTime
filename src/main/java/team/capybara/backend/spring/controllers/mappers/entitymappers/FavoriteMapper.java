package team.capybara.backend.spring.controllers.mappers.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.dto.favorite.FavoriteDto;
import team.capybara.backend.spring.controllers.mappers.Mapper;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.FavoriteConverter;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ProductTypeConverter;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.UserConverter;
import team.capybara.backend.spring.controllers.repositories.FavoriteRepository;
import team.capybara.backend.spring.entities.Favorite;
import team.capybara.backend.spring.entities.ProductType;
import team.capybara.backend.spring.entities.User;

import java.util.UUID;

@Component
public final class FavoriteMapper implements Mapper<Favorite, FavoriteDto> {
    private final FavoriteRepository favoriteRepository;
    private final FavoriteConverter favoriteConverter;
    private final UserConverter userConverter;
    private final ProductTypeConverter productTypeConverter;

    public FavoriteMapper(
            FavoriteRepository favoriteRepository,
            FavoriteConverter favoriteConverter,
            UserConverter userConverter,
            ProductTypeConverter productTypeConverter
    ) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteConverter = favoriteConverter;
        this.userConverter = userConverter;
        this.productTypeConverter = productTypeConverter;

    }

    @Override
    public FavoriteDto getEntity(Favorite favorite) {
        return new FavoriteDto(
                favorite.getId(),
                favorite.getUser().getId(),
                favorite.getProductType().getId()
        );
    }

    @Override
    public FavoriteDto postEntity(FavoriteDto favoriteToCreate) {
        try {
            User user = userConverter.toEntity(favoriteToCreate.userId());
            ProductType productType = productTypeConverter.toEntity(favoriteToCreate.productTypeId());

            Favorite favoriteCreated = favoriteRepository.save(new Favorite(
                    UUID.randomUUID(),
                    user,
                    productType
            ));

            return getEntity(favoriteCreated);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public FavoriteDto putEntity(FavoriteDto favoriteToUpdate) {
        try {
            Favorite favoriteUpdated = favoriteConverter.toEntity(favoriteToUpdate.id());
            User user = userConverter.toEntity(favoriteToUpdate.id());
            ProductType productType = productTypeConverter.toEntity(favoriteToUpdate.productTypeId());

            favoriteUpdated.setUser(user);
            favoriteUpdated.setProductType(productType);
            favoriteRepository.save(favoriteUpdated);

            return getEntity(favoriteUpdated);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public void deleteEntity(UUID entityId) {
        try {
            Favorite favoriteToDelete = favoriteConverter.toEntity(entityId);
            favoriteRepository.delete(favoriteToDelete);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
