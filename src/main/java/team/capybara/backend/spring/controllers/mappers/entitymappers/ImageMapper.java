package team.capybara.backend.spring.controllers.mappers.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;
import team.capybara.backend.spring.controllers.mappers.Mapper;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ImageConverter;
import team.capybara.backend.spring.controllers.repositories.ImageRepository;
import team.capybara.backend.spring.entities.Image;
import team.capybara.backend.spring.controllers.dto.image.ImageWithIdDto;

import java.util.*;

@Component
public final class ImageMapper implements Mapper<Image, ImageWithIdDto, ImageDto> {
    private final ImageRepository imageRepository;
    private final ImageConverter imageConverter;

    public ImageMapper(ImageRepository imageRepository, ImageConverter imageConverter) {
        this.imageRepository = imageRepository;
        this.imageConverter = imageConverter;
    }

    @Override
    public ImageWithIdDto getEntity(Image image) {
        return new ImageWithIdDto(
                image.getId(),
                image.getPath()
        );
    }

    @Override
    public ImageWithIdDto postEntity(ImageDto imageToCreate) {
        UUID id = UUID.randomUUID();
        Image imageCreated = imageRepository.save(new Image(
                id,
                imageToCreate.path() + id + ".jpg"
        ));

        return getEntity(imageCreated);
    }

    @Override
    public ImageWithIdDto putEntity(ImageWithIdDto imageToUpdate) {
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
        try {
            Image imageDeleted = imageConverter.toEntity(id);
            imageRepository.delete(imageDeleted);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public List<ImageWithIdDto> toDtoList(List<Image> images) {
        List<ImageWithIdDto> dtoImages = new LinkedList<>();

        for (Image image : images) {
            dtoImages.add(this.getEntity(image));
        }

        return dtoImages;
    }

    public List<Image> toEntityList(List<ImageWithIdDto> dtoImages) {
        List<Image> images = new LinkedList<>();

        for (ImageWithIdDto imageWithIdDto : dtoImages) {
            Optional<Image> optionalImage = imageRepository.findById(imageWithIdDto.id());
            if (optionalImage.isPresent()) {
                images.add(optionalImage.get());
            } else {
                throw new EntityNotFoundException("Image not found; id=" + imageWithIdDto.id());
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
