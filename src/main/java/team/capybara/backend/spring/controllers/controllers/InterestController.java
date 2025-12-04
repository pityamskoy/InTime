package team.capybara.backend.spring.controllers.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.entities.interest.InterestDto;
import team.capybara.backend.spring.controllers.dto.entities.interest.InterestWithIdDto;
import team.capybara.backend.spring.controllers.services.InterestService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/interests")
@SuppressWarnings(value = {"unused"})
public final class InterestController {
    private static final Logger log = LoggerFactory.getLogger(InterestController.class);

    private final InterestService interestService;

    public InterestController(InterestService interestService) {
        this.interestService = interestService;
    }

    @GetMapping
    public ResponseEntity<List<InterestWithIdDto>> getAllInterests() {
        log.info("Called getAllInterests");

        return ResponseEntity.ok(interestService.getAllInterests());
    }

    @GetMapping("/get_by_product/{id}")
    public ResponseEntity<List<InterestWithIdDto>> getAllInterestsByProduct(@PathVariable String id) {
        log.info("Called getAllInterestsByProduct");

        return ResponseEntity.ok(interestService.getAllInterestsByProduct(UUID.fromString(id)));
    }

    @GetMapping("/get_by_user/{id}")
    public ResponseEntity<List<InterestWithIdDto>> getAllInterestsByUser(@PathVariable String id) {
        log.info("Called getAllInterestsByUser");

        return ResponseEntity.ok(interestService.getAllInterestsByUser(UUID.fromString(id)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterestWithIdDto> getInterestById(@PathVariable String id) {
        log.info("Called getInterestById; id={}", id);
        Optional<InterestWithIdDto> interestDtoOptional = interestService.getInterestById(UUID.fromString(id));

        return interestDtoOptional.map(ResponseEntity::ok).orElseGet
                (() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<InterestWithIdDto> createInterest(@RequestBody InterestDto interestToCreate) {
        log.info("Called createInterest; interestToCreate={}", interestToCreate);

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(interestService.createInterest(interestToCreate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<InterestWithIdDto> updateInterest(@RequestBody InterestWithIdDto interestToUpdate) {
        log.info("Called updateInterest; interestToUpdate={}", interestToUpdate);

        try {
            return ResponseEntity.ok(interestService.updateInterest(interestToUpdate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteInterest(@PathVariable String id) {
        log.info("Called deleteInterest; id={}", id);

        try {
            interestService.deleteInterest(UUID.fromString(id));
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
