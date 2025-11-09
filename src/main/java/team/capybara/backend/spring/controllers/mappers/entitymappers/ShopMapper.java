package team.capybara.backend.spring.controllers.mappers.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mappers.Mapper;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ImageConverter;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ShopConverter;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;
import team.capybara.backend.spring.entities.Image;
import team.capybara.backend.spring.entities.Shop;
import team.capybara.backend.spring.controllers.dto.shop.ShopDto;

import java.util.List;
import java.util.UUID;

@Component
public final class ShopMapper implements Mapper<Shop, ShopDto> {
    private final ShopRepository shopRepository;
    private final ShopConverter shopConverter;
    private final ImageConverter imageConverter;

    public ShopMapper(
            ShopRepository shopRepository,
            ShopConverter shopConverter,
            ImageConverter imageConverter
    ) {
        this.shopRepository = shopRepository;
        this.shopConverter = shopConverter;
        this.imageConverter = imageConverter;
    }

    @Override
    public ShopDto getEntity(Shop shop) {
        List<UUID> imagesId = imageConverter.toIdList(shop.getImages());

        return new ShopDto(
                shop.getId(),
                shop.getName(),
                shop.getDescription(),
                shop.isVerifide(),
                shop.getMainImagePath(),
                imagesId,
                shop.getAddress(),
                shop.getLat(),
                shop.getLon()
        );
    }

    @Override
    public ShopDto postEntity(ShopDto shopToCreate) {
        try {
            List<Image> images = imageConverter.toEntityList(shopToCreate.imagesId());

            Shop shopCreated = shopRepository.save(new Shop(
                    UUID.randomUUID(),
                    shopToCreate.name(),
                    shopToCreate.description(),
                    shopToCreate.isVerified(),
                    shopToCreate.mainImagePath(),
                    images,
                    shopToCreate.address(),
                    shopToCreate.lat(),
                    shopToCreate.lon()
            ));

            return getEntity(shopCreated);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public ShopDto putEntity(ShopDto shopToUpdate) {
        Shop shopUpdated = shopConverter.toEntity(shopToUpdate.id());
        List<Image> images = imageConverter.toEntityList(shopToUpdate.imagesId());

        shopUpdated.setName(shopToUpdate.name());
        shopUpdated.setDescription(shopToUpdate.description());
        shopUpdated.setVerifide(shopToUpdate.isVerified());
        shopUpdated.setMainImagePath(shopToUpdate.mainImagePath());
        shopUpdated.setImages(images);
        shopUpdated.setAddress(shopToUpdate.address());
        shopUpdated.setLat(shopToUpdate.lat());
        shopUpdated.setLon(shopToUpdate.lon());
        shopRepository.save(shopUpdated);

        return getEntity(shopUpdated);
    }

    @Override
    public void deleteEntity(UUID id) {
        try {
            Shop shopDeleted = shopConverter.toEntity(id);
            shopRepository.delete(shopDeleted);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
