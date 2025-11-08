package team.capybara.backend.spring.controllers.services;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.FileFromStorageStore;
import team.capybara.backend.spring.controllers.dto.image.ImageBase64Dto;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ImageConverter;
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
    private final String imageStorePath = "./src/main/resources/static/";
    private final String imagePath = "http://127.0.0.1:8080/images/image_load/";
    private final FileFromStorageStore fileFromStorageStore;
    private final ImageConverter imageConverter;

    public ImageService(
            ImageMapper imageMapper,
            ImageRepository imageRepository,
            ImageConverter imageConverter
    ) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
        fileFromStorageStore = new FileFromStorageStore();
        this.imageConverter = imageConverter;
    }

    public Boolean saveImageData(ImageBase64Dto imageBase64,String id){
        //сохраняет Base64
        Boolean success = true;
        try{
            fileFromStorageStore.saveFile(imageStorePath,id,imageBase64.image().getBytes());
        }
        catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
        return success;
    }

    public byte[] getImageData(String id){
        byte[] imageData = null;
        try{
            imageData = fileFromStorageStore.readFile(imageStorePath+id);
        }
        catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
        return imageData;
    }

    public List<ImageDto> getAllImages() {
        List<Image> images = imageRepository.findAll();
        return images.stream().map(imageMapper::getEntity).toList();
    }

    public ImageDto getImageById(UUID id) {
        try {
            return imageMapper.getEntity(imageConverter.toEntity(id));
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    //fix soon
    public ImageDto createImage(ImageDto imageToCreate) {
        ImageDto imageCreated = imageMapper.postEntity(imageToCreate);
        Image image = imageConverter.toEntity(imageCreated.id());
        image.setPath(imagePath + image.getId().toString());
        imageRepository.save(image);

        return imageCreated;
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

