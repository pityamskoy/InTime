package team.capybara.backend.spring.controllers.mapping.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mapping.Mapper;
import team.capybara.backend.spring.controllers.repositories.ImageRepository;
import team.capybara.backend.spring.entities.Image;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;

import java.util.*;

@Component
public final class ImageMapper implements Mapper<Image, ImageDto> {
    ImageRepository imageRepository;

    public ImageMapper(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
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
        Optional<Image> image = imageRepository.findById(imageToUpdate.imageId());

        if (image.isEmpty()) {
            throw new EntityNotFoundException("Image not found; id=" + imageToUpdate.imageId());
        }

        Image obj = image.get();
        obj.setPath(imageToUpdate.path());
        imageRepository.save(obj);

        return getEntity(obj);
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
            Optional<Image> optionalImage = imageRepository.findById(imageDto.imageId());
            if (optionalImage.isPresent()) {
                images.add(optionalImage.get());
            } else {
                throw new EntityNotFoundException("Image not found; id=" + imageDto.imageId());
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
