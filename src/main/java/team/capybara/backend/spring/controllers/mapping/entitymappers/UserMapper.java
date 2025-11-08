package team.capybara.backend.spring.controllers.mapping.entitymappers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.dto.user.UserAuthDto;
import team.capybara.backend.spring.controllers.dto.user.UserDto;
import team.capybara.backend.spring.controllers.mapping.Mapper;
import team.capybara.backend.spring.controllers.repositories.UserRepository;
import team.capybara.backend.spring.entities.User;

import java.util.Optional;
import java.util.UUID;

@Component
public final class UserMapper {
    public UserDto getEntity(User user) {
        return new UserDto(
                user.getId(),
                user.getName()
        );
    }

    @Component
    public static final class UserAuthMapper implements Mapper<User, UserAuthDto> {
        private final UserRepository userRepository;

        public UserAuthMapper(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @Override
        public UserAuthDto getEntity(User user) {
            return new UserAuthDto(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getPassword()
            );
        }

        @Override
        public User postEntity(UserAuthDto userAuthToCreate) {
            return new User(
                    UUID.randomUUID(),
                    userAuthToCreate.name(),
                    userAuthToCreate.email(),
                    userAuthToCreate.password()
            );
        }

        @Override
        public User putEntity(UserAuthDto userAuthToUpdate) {
            Optional<User> user = userRepository.findById(userAuthToUpdate.id());

            if (user.isEmpty()) {
                throw new EntityNotFoundException("Not found user with id=" + userAuthToUpdate.id());
            }

            User obj = user.get();
            obj.setName(userAuthToUpdate.name());
            obj.setEmail(userAuthToUpdate.email());
            obj.setPassword(userAuthToUpdate.password());

            return obj;
        }

        @Override
        public void deleteEntity(UUID id) {
            Optional<User> user = userRepository.findById(id);

            if (user.isEmpty()) {
                throw new EntityNotFoundException("Not found user with id=" + id);
            }

            userRepository.deleteById(id);
        }
    }
}
