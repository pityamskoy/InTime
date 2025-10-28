package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.shop.ShopDto;
import team.capybara.backend.spring.controllers.mapping.ImageMapper;
import team.capybara.backend.spring.controllers.mapping.ShopMapper;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import team.capybara.backend.spring.controllers.repositories.ImageRepository;
import team.capybara.backend.spring.entitys.Image;
import team.capybara.backend.spring.entitys.Shop;

@Service
public class ShopService {
    private final ProductRepository productRepository;
    private final ShopMapper shopMapper;
    private final ImageMapper imageMapper;
    private final ShopRepository shopRepository;
    private final ImageRepository imageRepository;

    @Autowired
    ShopService(ProductRepository productRepository, ShopMapper shopMapper, ImageMapper imageMapper, ShopRepository shopRepository, ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.shopMapper = shopMapper;
        this.imageMapper = imageMapper;
        this.shopRepository = shopRepository;
        this.imageRepository = imageRepository;
    }

    public List<ShopDto> getAllShops() {
        List<Shop> shops = shopRepository.findAll();

        return shops.stream().map(shopMapper::toDto).toList();
    }

    public Optional<ShopDto> getShopById(String id) {
        Optional<Shop> shop = shopRepository.findById(UUID.fromString(id));

        if (shop.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat.format("Not found shop by id={0}", UUID.fromString(id)));
        }

        return Optional.ofNullable(shopMapper.toDto(shop.get()));
    }

    public Shop createShop(ShopDto shopToCreate) throws EntityNotFoundException {
        Shop shopToSave = shopMapper.postEntity(shopToCreate);

        for (Image img : shopToSave.getImages()) {
            imageRepository.save(img);
        }
        return shopRepository.save(shopToSave);
    }

    public Shop updateShop(ShopDto shopToUpdate) throws EntityNotFoundException {
        Optional<Shop> optionalProduct = shopRepository.findById(shopToUpdate.id());

        if (optionalProduct.isEmpty()) {
            throw new EntityNotFoundException("Not found product by id=" + shopToUpdate.id());
        } else {
            Shop shop = optionalProduct.get();

            shop.setAddress(shopToUpdate.description());
            shop.setDescription(shopToUpdate.description());
            shop.setImages(imageMapper.toEntityList (shopToUpdate.images()));
            shop.setMainImagePath(shopToUpdate.mainImagePath());
            shop.setLat(shopToUpdate.lat());
            shop.setLon(shopToUpdate.lon());
            shop.setName(shopToUpdate.name());
            shop.setVerifide(shopToUpdate.isVerified());

            return shopRepository.save(shop);
        }
    }

    public void deleteShop(String id) {
        if (!shopRepository.existsById(UUID.fromString(id))) {
            throw new EntityNotFoundException("Not found shop by id=" + id);
        }

        shopRepository.deleteById(UUID.fromString(id));
    }
}