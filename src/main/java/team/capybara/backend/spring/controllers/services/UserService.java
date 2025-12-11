package team.capybara.backend.spring.controllers.services;

import jakarta.annotation.Nullable;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.other.login.LoginDto;
import team.capybara.backend.spring.controllers.dto.other.login.LoginResultDto;
import team.capybara.backend.spring.controllers.dto.entities.user.UserAuthDto;
import team.capybara.backend.spring.controllers.dto.entities.user.UserAuthWithIdDto;
import team.capybara.backend.spring.controllers.dto.entities.user.UserDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.UserMapper;
import team.capybara.backend.spring.controllers.repositories.UserRepository;
import team.capybara.backend.spring.entities.User;
import team.capybara.backend.spring.controllers.controllers.UserController;

import javax.security.auth.login.CredentialException;
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

    public Optional<UserDto> getUserById(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            UserDto userDto = userMapper.getEntity(userOptional.get());
            return Optional.of(userDto);
        }

        return Optional.empty();
    }

    /**
     * @param username is a value of cookie, which {@link UserController} accepts as an argument.
     * @param loginDto is login credentials.
     * @return {@link Pair}<{@link Cookie}, {@link LoginResultDto}>, where {@link Cookie} is null if {@code String username} was provided.
     * @throws CredentialException if cookie is null and login credentials are null.
     */
    public Pair<Cookie,LoginResultDto> login(
            @Nullable String username,
            LoginDto loginDto
    ) throws CredentialException {
        if (username == null && (loginDto.login() == null || loginDto.password() == null)) {
            throw new CredentialException("Login credentials are missing.");
        }

        if (username != null) {
            if (loginDto.login() == null || loginDto.password() == null) {
                return new Pair<>(null, new LoginResultDto(true, username));
            }
        }

        String login = loginDto.login();
        Optional<User> userOptional;

        if (login.contains("@")) {
            userOptional = userRepository.findUserByEmail(login);
        } else {
            userOptional = userRepository.findUserByPhoneNumber(login);
        }

        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }

        User user = userOptional.get();
        LoginResultDto loginResultDto = new LoginResultDto(user.getPassword().equals(loginDto.password()), user.getId().toString());

        if (username != null) {
            if (username.equals(user.getId().toString())) {
                return new Pair<>(null, new LoginResultDto(true, username));
            } else {
                return new Pair<>(null, new LoginResultDto(false, username));
            }
        }

        if (loginResultDto.success()) {
            Cookie cookie = new Cookie("username", loginResultDto.userId());
            cookie.setPath("/users");
            cookie.setMaxAge(14400);
            cookie.setHttpOnly(false);
            cookie.setSecure(false);

            return new Pair<>(cookie, loginResultDto);
        }

        return new Pair<>(null, loginResultDto);
    }

    public Cookie logout(String username) {
        Cookie cookie = new Cookie("username", username);
        cookie.setPath("/users");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);

        return cookie;
    }

    public Pair<Cookie, UserAuthWithIdDto> register(UserAuthDto userToRegister) {
        try {
            UserAuthWithIdDto userRegistered = userAuthMapper.postEntity(userToRegister);
            Cookie cookie = new Cookie("username", userRegistered.id().toString());
            cookie.setPath("/users");
            cookie.setMaxAge(14400);
            cookie.setHttpOnly(false);
            cookie.setSecure(false);

            return new Pair<>(cookie, userRegistered);
        } catch (EntityExistsException e) {
            throw new EntityExistsException(e.getMessage());
        }
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
