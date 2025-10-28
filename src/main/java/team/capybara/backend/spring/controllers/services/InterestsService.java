package team.capybara.backend.spring.controllers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.repositories.*;
import team.capybara.backend.spring.entitys.Image;
import team.capybara.backend.spring.entitys.Interests;

import java.util.List;

@Service
public class InterestsService {
    private final InterestsRepository interestsRepository;
    private final ImageRepository imageRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    @Autowired
    public InterestsService(InterestsRepository interestsRepository, ImageRepository imageRepository, ProductTypeRepository productTypeRepository, ProductRepository productRepository, ShopRepository shopRepository, UserRepository userRepository) {
        this.interestsRepository = interestsRepository;
        this.imageRepository = imageRepository;
        this.productTypeRepository = productTypeRepository;
        this.productRepository = productRepository;
        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
    }

    public Interests createInteres(Interests interesToCreate) {
        Interests newInteres = new Interests(
                interesToCreate.getId(),
                interesToCreate.getUser(),
                interesToCreate.getProduct(),
                interesToCreate.getTime()
        );

        userRepository.save(newInteres.getUser());

        for(Image img:newInteres.getProduct().getProductType().getShop().getImages())
            imageRepository.save(img);

        for(Image img:newInteres.getProduct().getProductType().getImages())
            imageRepository.save(img);

        shopRepository.save(newInteres.getProduct().getProductType().getShop());
        productTypeRepository.save(newInteres.getProduct().getProductType());
        productRepository.save(newInteres.getProduct());
        return interestsRepository.save(newInteres);
    }

    public List<Interests> getAllInterests() {
        return interestsRepository.findAll();
    }
}