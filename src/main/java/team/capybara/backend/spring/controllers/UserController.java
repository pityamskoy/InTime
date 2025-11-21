package team.capybara.backend.spring.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.user.UserAuthDto;
import team.capybara.backend.spring.controllers.dto.user.UserDto;
import team.capybara.backend.spring.controllers.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@CrossOrigin(value = {"http://localhost:3000"})
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
    public ResponseEntity<UserAuthDto> getUserById(@PathVariable String id) {
        log.info("Called getUserById; id={}", id);
        Optional<UserAuthDto> userAuthDtoOptional = userService.getUserById(UUID.fromString(id));

        return userAuthDtoOptional.map(ResponseEntity::ok).orElseGet
                (() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/login")
    public ResponseEntity<Boolean> login(
            @RequestParam String login,
            @RequestParam String password
    ) {
        log.info("Called login; login={}, password={}", login, password);

        if (login.isEmpty() || password.isEmpty()) {
            return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).build();
        }

        return ResponseEntity.ok(userService.login(login, password));
    }

    @PostMapping("/create")
    public ResponseEntity<UserAuthDto> createUser(@RequestBody UserAuthDto userToCreate) {
        log.info("Called createUser; userToCreate={}", userToCreate);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(userToCreate));
    }

    @PutMapping("/update")
    public ResponseEntity<UserAuthDto> updateUser(@RequestBody UserAuthDto userToUpdate) {
        log.info("Called updateUser; userToUpdate={}", userToUpdate);

        try {
            return ResponseEntity.ok(userService.updateUser(userToUpdate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestBody String id) {
        log.info("Called deleteUser; id={}", id);

        try {
            userService.deleteUser(UUID.fromString(id));
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
