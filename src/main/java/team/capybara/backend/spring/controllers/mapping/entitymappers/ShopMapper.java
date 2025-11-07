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
    public Shop postEntity(ShopDto shopDto) {
        List<Image> images = imageMapper.toEntityList(shopDto.images());

        return new Shop(
                UUID.randomUUID(),
                shopDto.name(),
                shopDto.description(),
                shopDto.isVerified(),
                shopDto.mainImagePath(),
                images,
                shopDto.address(),
                shopDto.lat(),
                shopDto.lon()
        );
    }

    @Override
    public Shop putEntity(ShopDto dtoObject) {
        Optional<Shop> shop = shopRepository.findById(dtoObject.id());
        if (shop.isEmpty()) {
            throw new EntityNotFoundException("Not found shop, id=" + dtoObject.id());
        }

        Shop obj =  shop.get();
        obj.setName(dtoObject.name());
        obj.setDescription(dtoObject.description());
        obj.setVerifide(dtoObject.isVerified());
        obj.setMainImagePath(dtoObject.mainImagePath());
        obj.setImages(imageMapper.toEntityList(dtoObject.images()));
        obj.setAddress(dtoObject.address());
        obj.setLat(dtoObject.lat());
        obj.setLon(dtoObject.lon());

        return obj;
    }

    @Override
    public void deleteEntity(UUID entityId) {
        Optional<Shop> shop = shopRepository.findById(entityId);

        if (shop.isEmpty()) {
            throw new EntityNotFoundException("Not found shop, id=" + entityId);
        }

        shopRepository.deleteById(entityId);
    }
}
