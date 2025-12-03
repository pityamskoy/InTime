package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.web.server.Cookie;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.other.login.LoginResultDto;
import team.capybara.backend.spring.controllers.dto.entities.user.UserAuthDto;
import team.capybara.backend.spring.controllers.dto.entities.user.UserAuthWithIdDto;
import team.capybara.backend.spring.controllers.dto.entities.user.UserDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.UserMapper;
import team.capybara.backend.spring.controllers.repositories.UserRepository;
import team.capybara.backend.spring.entities.User;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public final class UserService {
    private final UserMapper userMapper;
    private final UserMapper.UserAuthMapper userAuthMapper;
    private final UserRepository userRepository;

    public UserService(
            UserMapper userMapper,
            UserMapper.UserAuthMapper userAuthMapper,
            UserRepository userRepository
    ) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.userAuthMapper = userAuthMapper;
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(userMapper::getEntity).toList();
    }

    public Optional<UserAuthWithIdDto> getUserById(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            UserAuthWithIdDto userAuthWithIdDto = userAuthMapper.getEntity(userOptional.get());
            return Optional.of(userAuthWithIdDto);
        }

        return Optional.empty();
    }

    public LoginResultDto login(String login, String password) {
        User user;

        if (login.contains("@")) {
            user = userRepository.findUserByEmail(login);
        } else {
            user = userRepository.findUserByPhoneNumber(login);
        }

        return new LoginResultDto(user.getPassword().equals(password), user.getId().toString());
    }

    public UserAuthWithIdDto createUser(UserAuthDto userToCreate) {
        return userAuthMapper.postEntity(userToCreate);
    }

    public UserAuthWithIdDto updateUser(UserAuthWithIdDto userToUpdate) {
        try {
            return userAuthMapper.putEntity(userToUpdate);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public void deleteUser(UUID id) {
        try {
            userAuthMapper.deleteEntity(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
