package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.entities.interest.InterestDto;
import team.capybara.backend.spring.controllers.dto.entities.interest.InterestWithIdDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.InterestMapper;
import team.capybara.backend.spring.controllers.repositories.*;
import team.capybara.backend.spring.entities.Interest;
import team.capybara.backend.spring.entities.Product;
import team.capybara.backend.spring.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public final class InterestService {
    private final InterestMapper interestMapper;
    private final InterestRepository interestRepository;
    private final ProductRepository productRepository ;
    private final UserRepository userRepository;

    public InterestService(
            InterestMapper interestMapper,
            InterestRepository interestRepository, ProductRepository productRepository, UserRepository userRepository
    ) {
        this.interestMapper = interestMapper;
        this.interestRepository = interestRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<InterestWithIdDto> getAllInterests() {
        List<Interest> interests = interestRepository.findAll();

        return interests.stream().map(interestMapper::getEntity).toList();
    }

    public List<InterestWithIdDto> getAllInterestsByProduct(UUID id) {
        Optional<Product> product = productRepository.findById(id);
        List<Interest> interests = interestRepository.findByProduct(product.get());

        return interests.stream().map(interestMapper::getEntity).toList();
    }

    public List<InterestWithIdDto> getAllInterestsByUser(UUID id) {
        Optional<User> user = userRepository.findById(id);
        List<Interest> interests = interestRepository.findByUser(user.get());

        return interests.stream().map(interestMapper::getEntity).toList();
    }

    public int getColOfInterestsByProduct(UUID id) {
        int col = interestRepository.colProductInInterests(id);
        return col;
    }

    public Boolean isReserved(UUID id_product, UUID id_user) {
        int isReserved = interestRepository.isProductInInterests(id_user,id_product);
        return isReserved > 0;
    }

    public Optional<InterestWithIdDto> getInterestById(UUID id) {
        Optional<Interest> interestOptional = interestRepository.findById(id);

        if (interestOptional.isPresent()) {
            InterestWithIdDto interestWithIdDto = interestMapper.getEntity(interestOptional.get());
            return Optional.of(interestWithIdDto);
        }

        return Optional.empty();
    }

    public InterestWithIdDto createInterest(InterestDto interestToCreate) {
        try {
            if(!isReserved(interestToCreate.productId(),interestToCreate.userId()))
                return interestMapper.postEntity(interestToCreate);
            throw new EntityNotFoundException();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public InterestWithIdDto updateInterest(InterestWithIdDto interestToUpdate) {
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