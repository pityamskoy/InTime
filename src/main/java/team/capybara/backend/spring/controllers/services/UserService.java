package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.user.UserAuthDto;
import team.capybara.backend.spring.controllers.dto.user.UserDto;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.UserConverter;
import team.capybara.backend.spring.controllers.mappers.entitymappers.UserMapper;
import team.capybara.backend.spring.controllers.repositories.UserRepository;
import team.capybara.backend.spring.controllers.services.exceptions.ServiceException;
import team.capybara.backend.spring.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public final class UserService {
    private final UserMapper userMapper;
    private final UserMapper.UserAuthMapper userAuthMapper;
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public UserService(
            UserMapper userMapper,
            UserMapper.UserAuthMapper userAuthMapper,
            UserRepository userRepository,
            UserConverter userConverter
    ) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.userAuthMapper = userAuthMapper;
        this.userConverter = userConverter;
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(userMapper::getEntity).toList();
    }

    public UserDto getUserById(UUID id) {
        try {
            User user = userConverter.toEntity(id);
            return userMapper.getEntity(user);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public UserAuthDto createUser(UserAuthDto userToCreate) {
        return userAuthMapper.postEntity(userToCreate);
    }

    public UserAuthDto updateUser(UserAuthDto userToUpdate) {
        try {
            return userAuthMapper.putEntity(userToUpdate);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteUser(UUID id) {
        try {
            userAuthMapper.deleteEntity(id);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
