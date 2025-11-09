package team.capybara.backend.spring.controllers.mappers.converters.entityconverters;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mappers.converters.EntityIdConverter;
import team.capybara.backend.spring.controllers.repositories.UserRepository;
import team.capybara.backend.spring.entities.User;

import java.util.Optional;
import java.util.UUID;

@Component
public final class UserConverter implements EntityIdConverter<User> {
    private final UserRepository userRepository;

    public UserConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User toEntity(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("User not found; id=" + id);
        }

        return userOptional.get();
    }
}
