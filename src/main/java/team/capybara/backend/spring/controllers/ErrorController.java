package team.capybara.backend.spring.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Deprecated(forRemoval = false)
@RestController
public class ErrorController {

    @GetMapping("/error")
    public void apiRoot() {
    }
}
