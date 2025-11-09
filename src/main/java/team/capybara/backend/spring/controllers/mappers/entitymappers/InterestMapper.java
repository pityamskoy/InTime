package team.capybara.backend.spring.controllers.mappers.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.dto.interest.InterestDto;
import team.capybara.backend.spring.controllers.mappers.Mapper;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.InterestConverter;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.ProductConverter;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.UserConverter;
import team.capybara.backend.spring.controllers.repositories.InterestRepository;
import team.capybara.backend.spring.entities.Interest;
import team.capybara.backend.spring.entities.Product;
import team.capybara.backend.spring.entities.User;

import java.util.UUID;

@Component
public final class InterestMapper implements Mapper<Interest, InterestDto> {
    private final InterestRepository interestRepository;
    private final InterestConverter interestConverter;
    private final UserConverter userConverter;
    private final ProductConverter productConverter;

    public InterestMapper(
            InterestRepository interestRepository,
            InterestConverter interestConverter,
            UserConverter userConverter,
            ProductConverter productConverter
    ) {
        this.interestRepository = interestRepository;
        this.interestConverter = interestConverter;
        this.userConverter = userConverter;
        this.productConverter = productConverter;
    }

    @Override
    public InterestDto getEntity(Interest interest) {
        return new InterestDto(
                interest.getId(),
                interest.getUser().getId(),
                interest.getProduct().getId(),
                interest.getTime()
        );
    }

    @Override
    public InterestDto postEntity(InterestDto interestToCreate) {
        try {
            User user = userConverter.toEntity(interestToCreate.userId());
            Product product = productConverter.toEntity(interestToCreate.productId());

            Interest interestCreated = interestRepository.save(new Interest(
                    UUID.randomUUID(),
                    user,
                    product,
                    interestToCreate.time()
            ));

            return getEntity(interestCreated);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public InterestDto putEntity(InterestDto interestToUpdate) {
        try {
            Interest interestUpdated = interestConverter.toEntity(interestToUpdate.id());
            User user = userConverter.toEntity(interestToUpdate.userId());
            Product product = productConverter.toEntity(interestToUpdate.productId());

            interestUpdated.setUser(user);
            interestUpdated.setProduct(product);
            interestUpdated.setTime(interestToUpdate.time());
            interestRepository.save(interestUpdated);

            return getEntity(interestUpdated);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public void deleteEntity(UUID entityId) {
        try {
            Interest interestDeleted = interestConverter.toEntity(entityId);
            interestRepository.delete(interestDeleted);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
