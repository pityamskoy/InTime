package team.capybara.backend.spring.controllers.services;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.FileFromStorageStore;
import team.capybara.backend.spring.controllers.dto.entities.image.ImageDto;
import team.capybara.backend.spring.controllers.dto.entities.image.ImageWithIdDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.ImageMapper;
import team.capybara.backend.spring.controllers.repositories.ImageRepository;
import team.capybara.backend.spring.entities.Image;

import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.Imaging;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public final class ImageService {
    private final ImageMapper imageMapper;
    private final ImageRepository imageRepository;
    private static final String imageStorePath = "./src/main/resources/static/";
    private static final String imagePath = "http://127.0.0.1:1235/";
    private final FileFromStorageStore fileFromStorageStore;

    public ImageService(
            ImageMapper imageMapper,
            ImageRepository imageRepository
    ) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
        fileFromStorageStore = new FileFromStorageStore();
    }

    public List<ImageWithIdDto> getAllImages() {
        List<Image> images = imageRepository.findAll();
        return images.stream().map(imageMapper::getEntity).toList();
    }

    public Optional<ImageWithIdDto> getImageById(UUID id) {
        Optional<Image> imageOptional = imageRepository.findById(id);

        if (imageOptional.isPresent()) {
            ImageWithIdDto imageWithIdDto = imageMapper.getEntity(imageOptional.get());
            return Optional.of(imageWithIdDto);
        }

        return Optional.empty();
    }

    public ImageWithIdDto createImage(byte[] imageToCreate) throws Exception {
        ImageWithIdDto imageCreated = imageMapper.postEntity(new ImageDto(imagePath));

        //byte[] convertedImageData = convertToWebP(imageToCreate);

        fileFromStorageStore.saveFile(imageStorePath,imageCreated.id().toString()+".webp",imageToCreate);
        return imageCreated;
    }

    public ImageWithIdDto updateImage(ImageWithIdDto imageToUpdate) {
        try {
            return imageMapper.putEntity(imageToUpdate);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public void deleteImage(UUID id) {
        try {
            imageMapper.deleteEntity(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}

