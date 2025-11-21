package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.interest.InterestDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.InterestMapper;
import team.capybara.backend.spring.controllers.repositories.*;
import team.capybara.backend.spring.entities.Interest;
import team.capybara.backend.spring.entities.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public final class InterestService {
    private final InterestMapper interestMapper;
    private final InterestRepository interestRepository;
    private final ProductRepository productRepository ;

    public InterestService(
            InterestMapper interestMapper,
            InterestRepository interestRepository, ProductRepository productRepository
    ) {
        this.interestMapper = interestMapper;
        this.interestRepository = interestRepository;
        this.productRepository = productRepository;
    }

    public List<InterestDto> getAllInterests() {
        List<Interest> interests = interestRepository.findAll();

        return interests.stream().map(interestMapper::getEntity).toList();
    }

    public List<InterestDto> getAllInterestsByProduct(UUID id) {
        Optional<Product> product = productRepository.findById(id);
        List<Interest> interests = interestRepository.findByProduct(product.get());

        return interests.stream().map(interestMapper::getEntity).toList();
    }

    public Optional<InterestDto> getInterestById(UUID id) {
        Optional<Interest> interestOptional = interestRepository.findById(id);

        if (interestOptional.isPresent()) {
            InterestDto interestDto = interestMapper.getEntity(interestOptional.get());
            return Optional.of(interestDto);
        }

        return Optional.empty();
    }

    public InterestDto createInterest(InterestDto interestToCreate) {
        try {
            return interestMapper.postEntity(interestToCreate);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public InterestDto updateInterest(InterestDto interestToUpdate) {
        try {
            return interestMapper.putEntity(interestToUpdate);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public void deleteInterest(UUID id) {
        try {
            interestMapper.deleteEntity(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}