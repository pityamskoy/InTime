package team.capybara.backend.spring.controllers.mappers.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mappers.Mapper;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ImageConverter;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ShopConverter;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.UserConverter;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;
import team.capybara.backend.spring.entities.Image;
import team.capybara.backend.spring.entities.Shop;
import team.capybara.backend.spring.controllers.dto.shop.ShopDto;
import team.capybara.backend.spring.entities.User;

import java.util.List;
import java.util.UUID;

@Component
public final class ShopMapper implements Mapper<Shop, ShopDto> {
    private final ShopRepository shopRepository;
    private final ShopConverter shopConverter;
    private final ImageConverter imageConverter;
    private final UserConverter userConverter;

    public ShopMapper(
            ShopRepository shopRepository,
            ShopConverter shopConverter,
            ImageConverter imageConverter,
            UserConverter userConverter
    ) {
        this.shopRepository = shopRepository;
        this.shopConverter = shopConverter;
        this.imageConverter = imageConverter;
        this.userConverter = userConverter;
    }

    @Override
    public ShopDto getEntity(Shop shop) {
        List<UUID> imagesId = imageConverter.toIdList(shop.getImages());

        return new ShopDto(
                shop.getId(),
                shop.getName(),
                shop.getTimeOpen(),
                shop.getTimeClose(),
                shop.getRegistrationDate(),
                shop.getDescription(),
                shop.isVerifide(),
                shop.getInn(),
                shop.getMainImagePath(),
                imagesId,
                shop.getAddress(),
                shop.getLat(),
                shop.getLon(),
                shop.getLinksToSocialMedia(),
                shop.getOwner().getId(),
                shop.getCompanyType(),
                shop.getDitance()
        );
    }

    @Override
    public ShopDto postEntity(ShopDto shopToCreate) {
        try {
            List<Image> images = imageConverter.toEntityList(shopToCreate.imagesId());
            User owner = userConverter.toEntity(shopToCreate.owner());

            Shop shopCreated = shopRepository.save(new Shop(
                    UUID.randomUUID(),
                    shopToCreate.name(),
                    shopToCreate.timeOpen(),
                    shopToCreate.timeClose(),
                    shopToCreate.registrationDate(),
                    shopToCreate.description(),
                    shopToCreate.isVerified(),
                    shopToCreate.inn(),
                    shopToCreate.mainImagePath(),
                    images,
                    shopToCreate.address(),
                    shopToCreate.lat(),
                    shopToCreate.lon(),
                    shopToCreate.linksToSocialMedia(),
                    owner,
                    shopToCreate.companyType(),
                    0.0
            ));

            return getEntity(shopCreated);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public ShopDto putEntity(ShopDto shopToUpdate) {
        try {
            Shop shopUpdated = shopConverter.toEntity(shopToUpdate.id());
            List<Image> images = imageConverter.toEntityList(shopToUpdate.imagesId());

            shopUpdated.setName(shopToUpdate.name());
            shopUpdated.setTimeOpen(shopToUpdate.timeOpen());
            shopUpdated.setTimeClose(shopToUpdate.timeClose());
            shopUpdated.setRegistrationDate(shopToUpdate.registrationDate());
            shopUpdated.setDescription(shopToUpdate.description());
            shopUpdated.setVerifide(shopToUpdate.isVerified());
            shopUpdated.setInn(shopToUpdate.inn());
            shopUpdated.setMainImagePath(shopToUpdate.mainImagePath());
            shopUpdated.setImages(images);
            shopUpdated.setAddress(shopToUpdate.address());
            shopUpdated.setLat(shopToUpdate.lat());
            shopUpdated.setLon(shopToUpdate.lon());
            shopUpdated.setLinksToSocialMedia(shopToUpdate.linksToSocialMedia());
            shopUpdated.setOwner(userConverter.toEntity(shopToUpdate.owner()));
            shopUpdated.setCompanyType(shopToUpdate.companyType());
            shopRepository.save(shopUpdated);

            return getEntity(shopUpdated);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
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
