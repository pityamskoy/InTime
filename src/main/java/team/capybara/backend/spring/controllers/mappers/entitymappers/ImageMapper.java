package team.capybara.backend.spring.controllers.mappers.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mappers.Mapper;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ImageConverter;
import team.capybara.backend.spring.controllers.repositories.ImageRepository;
import team.capybara.backend.spring.entities.Image;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;

import java.util.*;

@Component
public final class ImageMapper implements Mapper<Image, ImageDto> {
    private final ImageRepository imageRepository;
    private final ImageConverter imageConverter;

    public ImageMapper(ImageRepository imageRepository, ImageConverter imageConverter) {
        this.imageRepository = imageRepository;
        this.imageConverter = imageConverter;
    }

    @Override
    public ImageDto getEntity(Image image) {
        return new ImageDto(
                image.getId(),
                image.getPath()
        );
    }

    @Override
    public ImageDto postEntity(ImageDto imageToCreate) {
        Image imageCreated = imageRepository.save(new Image(
                UUID.randomUUID(),
                imageToCreate.path()
        ));

        return getEntity(imageCreated);
    }

    @Override
    public ImageDto putEntity(ImageDto imageToUpdate) {
        try {
            Image imageUpdated = imageConverter.toEntity(imageToUpdate.id());
            imageUpdated.setPath(imageToUpdate.path());
            imageRepository.save(imageUpdated);

            return getEntity(imageUpdated);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public void deleteEntity(UUID id) {
        if (!imageRepository.existsById(id)) {
            throw new EntityNotFoundException("Image not found; id=" + id);
        }

        imageRepository.deleteById(id);
    }

    public List<ImageDto> toDtoList(List<Image> images) {
        List<ImageDto> dtoImages = new LinkedList<>();

        for (Image image : images) {
            dtoImages.add(this.getEntity(image));
        }

        return dtoImages;
    }

    public List<Image> toEntityList(List<ImageDto> dtoImages) {
        List<Image> images = new LinkedList<>();

        for (ImageDto imageDto : dtoImages) {
            Optional<Image> optionalImage = imageRepository.findById(imageDto.id());
            if (optionalImage.isPresent()) {
                images.add(optionalImage.get());
            } else {
                throw new EntityNotFoundException("Image not found; id=" + imageDto.id());
            }
        }
        return images;
    }

    public List<Image> toEntityList(ArrayList<UUID> imagesId) {
        List<Image> images = new LinkedList<>();

        for (UUID imageId : imagesId) {
            Optional<Image> optionalImage = imageRepository.findById(imageId);
            if (optionalImage.isPresent()) {
                images.add(optionalImage.get());
            } else  {
                throw new EntityNotFoundException("Image not found; id=" + imageId);
            }
        }

        return images;
    }
}
