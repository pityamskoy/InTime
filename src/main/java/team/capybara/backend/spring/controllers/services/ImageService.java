package team.capybara.backend.spring.controllers.services;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.FileFromStorageStore;
import team.capybara.backend.spring.controllers.dto.image.ImageBase64Dto;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;
import team.capybara.backend.spring.controllers.mapping.entitymappers.ImageMapper;
import team.capybara.backend.spring.controllers.repositories.ImageRepository;
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

    public ImageService(ImageMapper imageMapper, ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
        fileFromStorageStore = new FileFromStorageStore();
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

    public Image createImage(ImageDto imageToCreate) {
        Image image = imageMapper.postEntity(imageToCreate);
        image.setPath(imagePath+image.getId().toString());
        return imageRepository.save(image);
    }

    public Image updateImage(ImageDto imageToUpdate) {
        try {
            return imageRepository.save(imageMapper.putEntity(imageToUpdate));
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

