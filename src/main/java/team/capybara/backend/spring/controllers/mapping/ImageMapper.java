package team.capybara.backend.spring.controllers.mapping;

import org.springframework.stereotype.Component;
import team.capybara.backend.spring.entitys.Image;
import team.capybara.backend.spring.controllers.dto.image.ImageDto;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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
                UUID.randomUUID(),
                imageDto.path()
        );
    }

    public List<ImageDto> toDtoList(List<Image> images) {
        List<ImageDto> dtoImages = new LinkedList<>();

        for (Image image : images) {
            dtoImages.add(this.toDto(image));
        }

        return dtoImages;
    }

    public List<Image> toEntityList(List<ImageDto> DtoImages) {
        List<Image> images = new LinkedList<>();

        for (ImageDto imageDto : DtoImages) {
            images.add(this.toEntity(imageDto));
        }

        return images;
    }
}
