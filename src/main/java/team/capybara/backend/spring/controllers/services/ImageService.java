package team.capybara.backend.spring.controllers.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.repositories.ImageRepository;
import team.capybara.backend.spring.entitys.Image;

import java.util.List;


@Service
public class ImageService {
    ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image createImage(Image image) {
        Image newImage = new Image(
                image.getId(),
                image.getPath()
                );

        return imageRepository.save(newImage);
    }

    public List<Image> getAllUsers() {
        return imageRepository.findAll();
    }
}

