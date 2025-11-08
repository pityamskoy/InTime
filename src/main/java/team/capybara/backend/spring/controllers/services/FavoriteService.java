package team.capybara.backend.spring.controllers.services;

import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.repositories.*;
import team.capybara.backend.spring.entities.Favorite;
import team.capybara.backend.spring.entities.Image;

import java.util.List;


@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(
            FavoriteRepository favoriteRepository
    ) {
        this.favoriteRepository = favoriteRepository;
    }

    public Favorite createFavorite(Favorite favoriteToCreate) {
        Favorite newFavorite = new Favorite(
                favoriteToCreate.getId(),
                favoriteToCreate.getUser(),
                favoriteToCreate.getProductType()
        );

        return favoriteRepository.save(newFavorite);
    }

    public List<Favorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }
}