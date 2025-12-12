package team.capybara.backend.spring.controllers.controllers;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.antlr.v4.runtime.misc.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.entities.user.*;
import team.capybara.backend.spring.controllers.dto.other.login.LoginDto;
import team.capybara.backend.spring.controllers.dto.other.login.LoginResultDto;
import team.capybara.backend.spring.controllers.services.UserService;

import javax.security.auth.login.CredentialException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
// @CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
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

    @GetMapping("/user")
    public ResponseEntity<UserDto> getUserById(@CookieValue(value = "username") String id) {
        log.info("Called getUserById; id={}", id);
        Optional<UserDto> userDtoOptional = userService.getUserById(UUID.fromString(id));

        return userDtoOptional.map(ResponseEntity::ok).orElseGet
                (() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/get_name_by_review_id/{id}")
    public ResponseEntity<Username> getUsernameByReviewId(@PathVariable String id) {
        log.info("Called getUserNameByReviewId; id={}", id);

        try {
            return ResponseEntity.ok(userService.getUsernameByReviewId(UUID.fromString(id)));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get_shop")
    public ResponseEntity<OwnershipDto> getShopByUserId(@CookieValue(value = "username") String id) {
        log.info("Called getShops; id={}", id);

        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            return ResponseEntity.ok(userService.getShopByUserId(UUID.fromString(id)));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResultDto> login(
            @CookieValue(required = false, value = "username") String username,
            @RequestBody LoginDto loginDto,
            HttpServletResponse response
    ) {
        log.info("Called login; loginDto={}", loginDto);

        try {
            Pair<Cookie, LoginResultDto> result = userService.login(username, loginDto);

            if (result.a != null) {
                response.addCookie(result.a);
            }

            return ResponseEntity.ok(result.b);
        } catch (CredentialException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/logout")
    public ResponseEntity<LoginResultDto> logout(
            @CookieValue(value = "username") String username,
            HttpServletResponse response
    ) {
        Cookie cookie = userService.logout(username);
        response.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserAuthWithIdDto> register(
            @RequestBody UserAuthDto userToRegister,
            HttpServletResponse response
    ) {
        log.info("Called register; userToRegister={}", userToRegister);

        try {
            Pair<Cookie, UserAuthWithIdDto> result = userService.register(userToRegister);
            response.addCookie(result.a);

            return ResponseEntity.status(HttpStatus.CREATED).body(result.b);
        } catch (EntityExistsException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
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
}
