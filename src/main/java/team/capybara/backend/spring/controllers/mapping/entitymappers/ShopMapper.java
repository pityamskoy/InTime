package team.capybara.backend.spring.controllers.mapping.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mapping.Mapper;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;
import team.capybara.backend.spring.entities.Image;
import team.capybara.backend.spring.entities.Shop;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;
import team.capybara.backend.spring.controllers.dto.shop.ShopDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public final class ShopMapper implements Mapper<Shop, ShopDto> {
    private final ShopRepository shopRepository;
    private final ImageMapper imageMapper;

    public ShopMapper(
            ImageMapper imageMapper,
            ShopRepository shopRepository
    ) {
        this.imageMapper = imageMapper;
        this.shopRepository = shopRepository;
    }

    @Override
    public ShopDto getEntity(Shop shop) {
        List<ImageDto> images = imageMapper.toDtoList(shop.getImages());

        return new ShopDto(
                shop.getId(),
                shop.getName(),
                shop.getDescription(),
                shop.isVerifide(),
                shop.getMainImagePath(),
                images,
                shop.getAddress(),
                shop.getLat(),
                shop.getLon()
        );
    }

    @Override
    public Shop postEntity(ShopDto shopToCreate) {
        List<Image> images = imageMapper.toEntityList(shopToCreate.images());

        return new Shop(
                UUID.randomUUID(),
                shopToCreate.name(),
                shopToCreate.description(),
                shopToCreate.isVerified(),
                shopToCreate.mainImagePath(),
                images,
                shopToCreate.address(),
                shopToCreate.lat(),
                shopToCreate.lon()
        );
    }

    @Override
    public Shop putEntity(ShopDto shopToUpdate) {
        Optional<Shop> shop = shopRepository.findById(shopToUpdate.id());

        if (shop.isEmpty()) {
            throw new EntityNotFoundException("Not found shop, id=" + shopToUpdate.id());
        }

        Shop obj = shop.get();
        obj.setName(shopToUpdate.name());
        obj.setDescription(shopToUpdate.description());
        obj.setVerifide(shopToUpdate.isVerified());
        obj.setMainImagePath(shopToUpdate.mainImagePath());
        obj.setImages(imageMapper.toEntityList(shopToUpdate.images()));
        obj.setAddress(shopToUpdate.address());
        obj.setLat(shopToUpdate.lat());
        obj.setLon(shopToUpdate.lon());

        return obj;
    }

    @Override
    public void deleteEntity(UUID id) {
        Optional<Shop> shop = shopRepository.findById(id);

        if (shop.isEmpty()) {
            throw new EntityNotFoundException("Not found shop, id=" + id);
        }

        shopRepository.deleteById(id);
    }
}
