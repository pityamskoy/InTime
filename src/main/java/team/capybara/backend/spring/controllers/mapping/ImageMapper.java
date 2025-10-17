package team.capybara.backend.spring.controllers.mapping;

import org.springframework.stereotype.Component;
import team.capybara.backend.hibernate.Image;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;

@Component
public class ImageMapper implements Mapper<Image, ImageDto> {

    @Override
    public ImageDto toDto(Image image) {
        return new  ImageDto(
                image.getId(),
                image.getPath()
        );
    }

    @Override
    public Image toEntity(ImageDto imageDto) {
        return new Image(
                imageDto.path()
        );
    }
}
