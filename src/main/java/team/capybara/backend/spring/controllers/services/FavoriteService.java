package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.favorite.FavoriteDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.FavoriteMapper;
import team.capybara.backend.spring.controllers.repositories.*;
import team.capybara.backend.spring.entities.Favorite;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public final class FavoriteService {
    private final FavoriteMapper favoriteMapper;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(
            FavoriteMapper favoriteMapper,
            FavoriteRepository favoriteRepository
    ) {
        this.favoriteMapper = favoriteMapper;
        this.favoriteRepository = favoriteRepository;
    }

    public List<FavoriteDto> getAllFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();

        return favorites.stream().map(favoriteMapper::getEntity).toList();
    }

    public Optional<FavoriteDto> getFavoriteById(UUID id) {
        Optional<Favorite> favorite = favoriteRepository.findById(id);

        if (favorite.isPresent()) {
            FavoriteDto favoriteDto = favoriteMapper.getEntity(favorite.get());
            return Optional.of(favoriteDto);
        }

        return Optional.empty();
    }

    public FavoriteDto createFavorite(FavoriteDto favoriteToCreate) {
        try {
            return favoriteMapper.postEntity(favoriteToCreate);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public FavoriteDto updateFavorite(FavoriteDto favoriteToUpdate) {
        try {
            return favoriteMapper.postEntity(favoriteToUpdate);
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