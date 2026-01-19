package team.capybara.backend.spring.controllers.mappers.converters.entityconverters;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mappers.converters.EntityIdConverter;
import team.capybara.backend.spring.controllers.repositories.InterestRepository;
import team.capybara.backend.spring.entities.Interest;

import java.util.Optional;
import java.util.UUID;

@Component
public final class InterestConverter implements EntityIdConverter<Interest> {
    private final InterestRepository interestRepository;

    public InterestConverter(InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }

    @Override
    public Interest toEntity(UUID id) {
        Optional<Interest> interestOptional = interestRepository.findById(id);

        if (interestOptional.isEmpty()) {
            throw new EntityNotFoundException("Interest not fount; id=" + id);
        }

        return interestOptional.get();
    }
}
