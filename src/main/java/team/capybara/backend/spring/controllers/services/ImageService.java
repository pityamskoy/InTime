package team.capybara.backend.spring.controllers.services;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.ImageMapper;
import team.capybara.backend.spring.controllers.repositories.ImageRepository;
import team.capybara.backend.spring.controllers.services.exceptions.ServiceException;
import team.capybara.backend.spring.entities.Image;

import java.util.List;
import java.util.UUID;


@Service
public final class ImageService {
    private final ImageMapper imageMapper;
    private final ImageRepository imageRepository;

    public ImageService(ImageMapper imageMapper, ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
    }

    public List<ImageDto> getAllImages() {
        List<Image> images = imageRepository.findAll();
        return images.stream().map(imageMapper::getEntity).toList();
    }

    public ImageDto createImage(ImageDto imageToCreate) {
        return imageMapper.postEntity(imageToCreate);
    }

    public ImageDto updateImage(ImageDto imageToUpdate) {
        try {
            return imageMapper.putEntity(imageToUpdate);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteImage(UUID id) {
        try {
            imageMapper.deleteEntity(id);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}

