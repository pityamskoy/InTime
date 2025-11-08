package team.capybara.backend.spring.controllers.services;

import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.repositories.*;
import team.capybara.backend.spring.entities.Image;
import team.capybara.backend.spring.entities.Interests;

import java.util.List;

@Service
public class InterestsService {
    private final InterestsRepository interestsRepository;

    public InterestsService(
            InterestsRepository interestsRepository
    ) {
        this.interestsRepository = interestsRepository;
    }

    public Interests createInteres(Interests interesToCreate) {
        Interests newInteres = new Interests(
                interesToCreate.getId(),
                interesToCreate.getUser(),
                interesToCreate.getProduct(),
                interesToCreate.getTime()
        );

        return interestsRepository.save(newInteres);
    }

    public List<Interests> getAllInterests() {
        return interestsRepository.findAll();
    }
}