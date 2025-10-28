package team.capybara.backend.spring.controllers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.repositories.*;
import team.capybara.backend.spring.entitys.Favorite;
import team.capybara.backend.spring.entitys.Image;

import java.util.List;


@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final ImageRepository imageRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepository, ImageRepository imageRepository, ProductTypeRepository productTypeRepository, ShopRepository shopRepository, UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.imageRepository = imageRepository;
        this.productTypeRepository = productTypeRepository;
        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
    }

    public Favorite createFavorite(Favorite favoriteToCreate) {
        Favorite newFavorite = new Favorite(
                favoriteToCreate.getId(),
                favoriteToCreate.getUser(),
                favoriteToCreate.getProductType()
        );

        userRepository.save(newFavorite.getUser());

        for (Image img : newFavorite.getProductType().getShop().getImages())
            imageRepository.save(img);

        for (Image img : newFavorite.getProductType().getImages())
            imageRepository.save(img);

        shopRepository.save(newFavorite.getProductType().getShop());
        productTypeRepository.save(newFavorite.getProductType());

        return favoriteRepository.save(newFavorite);
    }

    public List<Favorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }
}