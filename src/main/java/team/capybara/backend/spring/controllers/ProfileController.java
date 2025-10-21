package team.capybara.backend.spring.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.hibernate.User;
import team.capybara.backend.spring.controllers.services.UserService;

import java.util.List;

@RestController
@CrossOrigin(value = {"http://localhost:3000"})
@RequestMapping("/users")
@SuppressWarnings(value = {"unused"})
public class ProfileController {
    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    public ResponseEntity<List<User>> getAllUsers(@PathVariable String id) {
        log.info("Called getAllUsers");
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Called createUser");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(user));
    }
}
