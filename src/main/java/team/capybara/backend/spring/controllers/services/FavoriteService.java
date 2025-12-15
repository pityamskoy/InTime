package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.entities.favorite.FavoriteDto;
import team.capybara.backend.spring.controllers.dto.entities.favorite.FavoriteWithIdDto;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.UserConverter;
import team.capybara.backend.spring.controllers.mappers.entitymappers.FavoriteMapper;
import team.capybara.backend.spring.controllers.repositories.*;
import team.capybara.backend.spring.entities.Favorite;
import team.capybara.backend.spring.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public final class FavoriteService {
    private final FavoriteMapper favoriteMapper;
    private final UserConverter userConverter;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(
            FavoriteMapper favoriteMapper,
            UserConverter userConverter,
            FavoriteRepository favoriteRepository
    ) {
        this.favoriteMapper = favoriteMapper;
        this.userConverter = userConverter;
        this.favoriteRepository = favoriteRepository;
    }

    public List<FavoriteWithIdDto> getAllFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();

        return favorites.stream().map(favoriteMapper::getEntity).toList();
    }

    public Optional<FavoriteWithIdDto> getFavoriteById(UUID id) {
        Optional<Favorite> favorite = favoriteRepository.findById(id);

        if (favorite.isPresent()) {
            FavoriteWithIdDto favoriteWithIdDto = favoriteMapper.getEntity(favorite.get());
            return Optional.of(favoriteWithIdDto);
        }

        return Optional.empty();
    }

    public String isFavorite(UUID id_product_type, UUID id_user) {
        int isFavorite = favoriteRepository.isProductTypeInFavorites(id_user,id_product_type);
        String res = "";
        if (isFavorite > 0) {
            List<Favorite>favorites = favoriteRepository.findFavoritesByUser(userConverter.toEntity(id_user));
            for (Favorite el : favorites) {
                if (el.getProductType().getId().equals(id_product_type)) {
                    res = el.getId().toString();
                    break;
                }
            }
        }

        return res;
    }

    /**
     * {@code getFavoritesByUserId} should be package-private because it returns {@code Favorite}, not {@code FavoriteDto}.
     * This method is supposed to be used only in services to get auxiliary information.
     */
    List<Favorite> getFavoritesByUser(User user) {
        return favoriteRepository.findFavoritesByUser(user);
    }

    public List<FavoriteWithIdDto> getFavoritesByUserId(UUID id) {
        User user = userConverter.toEntity(id);
        try {
            return favoriteRepository.findFavoritesByUser(user).
                    stream().map(favoriteMapper::getEntity).toList();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public FavoriteWithIdDto createFavorite(FavoriteDto favoriteToCreate) {
        try {
            int isExist = favoriteRepository.isProductTypeInFavorites(favoriteToCreate.userId(),favoriteToCreate.productTypeId());
            if(isExist==0)
                return favoriteMapper.postEntity(favoriteToCreate);
            throw new EntityNotFoundException();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public FavoriteWithIdDto updateFavorite(FavoriteWithIdDto favoriteToUpdate) {
        try {
            return favoriteMapper.putEntity(favoriteToUpdate);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public void deleteFavorite(UUID id) {
        try {
            favoriteRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}