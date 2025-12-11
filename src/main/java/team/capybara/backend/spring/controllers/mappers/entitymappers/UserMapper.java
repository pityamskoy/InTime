package team.capybara.backend.spring.controllers.mappers.entitymappers;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.dto.entities.user.UserAuthDto;
import team.capybara.backend.spring.controllers.dto.entities.user.UserAuthWithIdDto;
import team.capybara.backend.spring.controllers.dto.entities.user.UserDto;
import team.capybara.backend.spring.controllers.mappers.Mapper;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.UserConverter;
import team.capybara.backend.spring.controllers.repositories.UserRepository;
import team.capybara.backend.spring.entities.User;

import java.util.Optional;
import java.util.UUID;

@Component
public final class UserMapper {
    public UserDto getEntity(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getPhoneNumber(),
                user.getIsShopOwner()
        );
    }

    @Component
    public static final class UserAuthMapper implements Mapper<User, UserAuthWithIdDto, UserAuthDto> {
        private final UserRepository userRepository;
        private final UserConverter userConverter;

        public UserAuthMapper(UserRepository userRepository, UserConverter userConverter) {
            this.userRepository = userRepository;
            this.userConverter = userConverter;
        }

        @Override
        public UserAuthWithIdDto getEntity(User user) {
            return new UserAuthWithIdDto(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getPassword(),
                    user.getIsShopOwner()
            );
        }

        @Override
        public UserAuthWithIdDto postEntity(UserAuthDto userAuthToRegister) {
            Optional<User> userFoundByEmail = userRepository.findUserByEmail(userAuthToRegister.email());
            Optional<User> userFoundByPhoneNumber = userRepository.findUserByPhoneNumber(userAuthToRegister.phoneNumber());

            if (userFoundByEmail.isPresent() || userFoundByPhoneNumber.isPresent()) {
                throw new EntityExistsException("User with such email or phone number already exists");
            }

            User userCreated = new User(
                    UUID.randomUUID(),
                    userAuthToRegister.name(),
                    userAuthToRegister.email(),
                    userAuthToRegister.phoneNumber(),
                    userAuthToRegister.password(),
                    userAuthToRegister.isShopOwner()
            );

            userRepository.save(userCreated);

            return getEntity(userCreated);
        }

        @Override
        public UserAuthWithIdDto putEntity(UserAuthWithIdDto userAuthToUpdate) {
            try {
                User userUpdated = userConverter.toEntity(userAuthToUpdate.id());

                userUpdated.setName(userAuthToUpdate.name());
                userUpdated.setEmail(userAuthToUpdate.email());
                userUpdated.setPhoneNumber(userAuthToUpdate.phoneNumber());
                userUpdated.setPassword(userAuthToUpdate.password());
                userUpdated.setIsShopOwner(userAuthToUpdate.isShopOwner());
                userRepository.save(userUpdated);

                return getEntity(userUpdated);
            } catch (EntityNotFoundException e) {
                throw new EntityNotFoundException(e.getMessage());
            }
        }

        @Override
        public void deleteEntity(UUID id) {
            try {
                User userDeleted = userConverter.toEntity(id);
                userRepository.delete(userDeleted);
            } catch (EntityNotFoundException e) {
                throw new EntityNotFoundException(e.getMessage());
            }
        }
    }
}
