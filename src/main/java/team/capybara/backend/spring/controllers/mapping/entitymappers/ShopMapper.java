package team.capybara.backend.spring.controllers.mapping.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mapping.Mapper;
import team.capybara.backend.spring.controllers.repositories.ImageRepository;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;
import team.capybara.backend.spring.entities.Image;
import team.capybara.backend.spring.entities.Shop;
import team.capybara.backend.spring.controllers.dto.shop.ShopDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public final class ShopMapper implements Mapper<Shop, ShopDto> {
    private final ShopRepository shopRepository;
    private final ImageRepository imageRepository;

    public ShopMapper(
            ShopRepository shopRepository,
            ImageRepository imageRepository
    ) {
        this.shopRepository = shopRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public ShopDto getEntity(Shop shop) {
        List<Image> images = shop.getImages();
        List<UUID> imagesId = new ArrayList<>();

        for (Image image : images) {
            imagesId.add(image.getId());
        }

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
    public Shop postEntity(ShopDto shopToCreate) {
        List<UUID> imagesId = shopToCreate.imagesId();
        List<Image> images = new ArrayList<>();

        for (UUID imageId : imagesId) {
            Optional<Image> image = imageRepository.findById(imageId);

            if (image.isEmpty()) {
                throw new EntityNotFoundException("Image not found; id=" + imageId);
            }
            images.add(image.get());
        }

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
            throw new EntityNotFoundException("Not found shop; id=" + shopToUpdate.id());
        }

        List<UUID> imagesId = shopToUpdate.imagesId();
        List<Image> images = new ArrayList<>();

        for (UUID imageId : imagesId) {
            Optional<Image> image = imageRepository.findById(imageId);

            if (image.isEmpty()) {
                throw new EntityNotFoundException("Image not found; id=" + imageId);
            }
            images.add(image.get());
        }

        Shop obj = shop.get();
        obj.setName(shopToUpdate.name());
        obj.setDescription(shopToUpdate.description());
        obj.setVerifide(shopToUpdate.isVerified());
        obj.setMainImagePath(shopToUpdate.mainImagePath());
        obj.setImages(images);
        obj.setAddress(shopToUpdate.address());
        obj.setLat(shopToUpdate.lat());
        obj.setLon(shopToUpdate.lon());

        return obj;
    }

    @Override
    public void deleteEntity(UUID id) {
        Optional<Shop> shop = shopRepository.findById(id);

        if (shop.isEmpty()) {
            throw new EntityNotFoundException("Not found shop; id=" + id);
        }

        shopRepository.deleteById(id);
    }
}
