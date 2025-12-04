package team.capybara.backend.spring.controllers.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.other.login.LoginDto;
import team.capybara.backend.spring.controllers.dto.other.login.LoginResultDto;
import team.capybara.backend.spring.controllers.dto.entities.user.UserAuthDto;
import team.capybara.backend.spring.controllers.dto.entities.user.UserAuthWithIdDto;
import team.capybara.backend.spring.controllers.dto.entities.user.UserDto;
import team.capybara.backend.spring.controllers.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@SuppressWarnings(value = {"unused"})
public final class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("Called getAllUsers");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAuthWithIdDto> getUserById(@PathVariable String id) {
        log.info("Called getUserById; id={}", id);
        Optional<UserAuthWithIdDto> userAuthDtoOptional = userService.getUserById(UUID.fromString(id));

        return userAuthDtoOptional.map(ResponseEntity::ok).orElseGet
                (() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResultDto> login(@CookieValue(value = "isLoggedIn", required = false) String isLoggedIn,  @RequestBody LoginDto loginDto, HttpServletResponse response) {

        if (isLoggedIn != null) {
            return ResponseEntity.ok().build(); // I don't, what I should do in this situation
        }

        String login = loginDto.login();
        String password = loginDto.password();
        log.info("Called login; login={}, password={}", login, password);

        if (login.isEmpty() || password.isEmpty()) {
            return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).build();
        }

        Cookie cookie = new Cookie("isLoggedIn", "True");
        cookie.setMaxAge(7200);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(userService.login(login, password));
    }

    @PostMapping("/create")
    public ResponseEntity<UserAuthWithIdDto> createUser(@RequestBody UserAuthDto userToCreate) {
        log.info("Called createUser; userToCreate={}", userToCreate);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(userToCreate));
    }

    @PutMapping("/update")
    public ResponseEntity<UserAuthWithIdDto> updateUser(@RequestBody UserAuthWithIdDto userToUpdate) {
        log.info("Called updateUser; userToUpdate={}", userToUpdate);

        try {
            return ResponseEntity.ok(userService.updateUser(userToUpdate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        log.info("Called deleteUser; id={}", id);

        try {
            userService.deleteUser(UUID.fromString(id));
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/logout")
    public String logout(@CookieValue(value = "isLoggedIn", required = false) String isLoggedIn, HttpServletResponse response) {
        if (isLoggedIn == null) {
            return "You are not logged in.";
        }

        Cookie cookie = new Cookie("token", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "Logout successful!";
    }
}
