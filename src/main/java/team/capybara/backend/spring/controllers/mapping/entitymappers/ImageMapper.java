package team.capybara.backend.spring.controllers.mapping.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mapping.Mapper;
import team.capybara.backend.spring.controllers.repositories.ImageRepository;
import team.capybara.backend.spring.entities.Image;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public final class ImageMapper implements Mapper<Image, ImageDto> {
    ImageRepository imageRepository;

    public ImageMapper(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }


    @Override
    public ImageDto getEntity(Image image) {
        return new  ImageDto(
                image.getId(),
                image.getPath()
        );
    }

    @Override
    public Image postEntity(ImageDto imageDto) {
        return new Image(
                UUID.randomUUID(),
                imageDto.path()
        );
    }

    @Override
    public Image putEntity(ImageDto dtoObjectWithId) {
        Optional<Image> image = imageRepository.findById(dtoObjectWithId.imageId());
        if (image.isPresent()) {
            Image obj = image.get();
            obj.setPath(dtoObjectWithId.path());
            return obj;
        } else {
            throw new EntityNotFoundException("Image not found with id: " + dtoObjectWithId.imageId());
        }
    }

    @Override
    public void removeEntity(UUID entityId) {
        if (!imageRepository.existsById(entityId)) {
            throw new EntityNotFoundException("Image not found with id: " + entityId);
        }

        imageRepository.deleteById(entityId);
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
            Optional<Image> optionalImage = imageRepository.findById(imageDto.imageId());
            if (optionalImage.isPresent()) {
                images.add(optionalImage.get());
            } else {
                throw new EntityNotFoundException("Image not found with id: " + imageDto.imageId());
            }
        }
        return images;
    }
}
