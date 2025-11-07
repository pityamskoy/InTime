package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.shop.ShopDto;
import team.capybara.backend.spring.controllers.mapping.entitymappers.ImageMapper;
import team.capybara.backend.spring.controllers.mapping.entitymappers.ShopMapper;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import team.capybara.backend.spring.entities.Shop;

@Service
public class ShopService {
    private final ShopMapper shopMapper;
    private final ShopRepository shopRepository;

    public ShopService(
            ShopMapper shopMapper,
            ShopRepository shopRepository
    ) {
        this.shopMapper = shopMapper;
        this.shopRepository = shopRepository;
    }

    public List<ShopDto> getAllShops() {
        List<Shop> shops = shopRepository.findAll();

        return shops.stream().map(shopMapper::getEntity).toList();
    }

    public Optional<ShopDto> getShopById(UUID id) {
        Optional<Shop> shop = shopRepository.findById(id);

        if (shop.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat.format("Not found shop by id={0}", id));
        }

        return Optional.of(shopMapper.getEntity(shop.get()));
    }

    public Shop createShop(ShopDto shopToCreate) {
        Shop shopToSave = shopMapper.postEntity(shopToCreate);

        return shopRepository.save(shopToSave);
    }

    public Shop updateShop(ShopDto shopToUpdate){
        try {
            return shopMapper.putEntity(shopToUpdate);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteShop(UUID id) {
        try {
            shopMapper.deleteEntity(id);
        }  catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}