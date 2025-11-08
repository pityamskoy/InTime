package team.capybara.backend.spring.controllers.mappers.converters.entityconverters;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mappers.converters.EntityListIdConverter;
import team.capybara.backend.spring.controllers.repositories.ImageRepository;
import team.capybara.backend.spring.entities.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public final class ImageConverter implements EntityListIdConverter<Image> {
    private final ImageRepository imageRepository;

    public ImageConverter(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public List<UUID> toIdList(List<Image> images) {
        List<UUID> imagesId = new ArrayList<>();

        for (Image image : images) {
            imagesId.add(image.getId());
        }

        return imagesId;
    }

    @Override
    public List<Image> toEntityList(List<UUID> imagesId) {
        List<Image> images = new ArrayList<>();

        for (UUID imageId : imagesId) {
            Optional<Image> image = imageRepository.findById(imageId);

            if (image.isEmpty()) {
                throw new EntityNotFoundException("Image not found; id=" + imageId);
            }

            images.add(image.get());
        }

        return images;
    }

    @Override
    public Image toEntity(UUID id) {
        Optional<Image> image = imageRepository.findById(id);

        if (image.isEmpty()) {
            throw new EntityNotFoundException("Image not found; id=" + id);
        }

        return image.get();
    }
}
