package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.controllers.pagination.PaginationHandler;
import team.capybara.backend.spring.controllers.dto.entities.product.ProductDto;
import team.capybara.backend.spring.controllers.repositories.FavoriteRepository;
import team.capybara.backend.spring.controllers.repositories.InterestRepository;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;
import team.capybara.backend.spring.entities.Favorite;
import team.capybara.backend.spring.entities.Interest;
import team.capybara.backend.spring.entities.Product;
import team.capybara.backend.spring.controllers.dto.entities.product.ProductWithIdDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.ProductMapper;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;
import team.capybara.backend.spring.entities.ProductType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public final class ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final InterestRepository interestRepository;
    private final FavoriteRepository favoriteRepository;
    private final PaginationHandler<Product> paginationHandler;

    public ProductService(
            ProductMapper productMapper,
            ProductRepository productRepository,
            ProductTypeRepository productTypeRepository, InterestRepository interestRepository, FavoriteRepository favoriteRepository,
            PaginationHandler<Product> paginationHandler
    ) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
        this.interestRepository = interestRepository;
        this.favoriteRepository = favoriteRepository;
        this.paginationHandler = paginationHandler;
    }

    public Page<ProductWithIdDto> getAllProducts(int offset, int limit) {
        Page<Product> products = productRepository.findAll(PageRequest.of(offset, limit));
        return products.map(productMapper::getEntity);
    }

    public Optional<ProductWithIdDto> getProductById(UUID id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            ProductWithIdDto productWithIdDto = productMapper.getEntity(productOptional.get());
            return Optional.of(productWithIdDto);
        }

        return Optional.empty();
    }

    public Page<ProductWithIdDto> getProductsByShop(int offset, int limit, String id) {
        List<ProductType> productTypes = productTypeRepository.findAll();
        List<ProductType> productTypesWithNeededShop = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        for (ProductType productType : productTypes) {
            if (productType.getShop().getId().equals(UUID.fromString(id)))
                productTypesWithNeededShop.add(productType);
        }

        for (ProductType productType : productTypesWithNeededShop) {
            products.addAll(productRepository.findByProductType(productType));
        }

        List<Product> slice = paginationHandler.makeSliceFromList(products, offset, limit);

        if (slice.isEmpty()) {
            return new PageImpl<>(new ArrayList<>());
        }
        //products.stream().forEach(product -> product.calculateScore(1,5));
        return new PageImpl<>(slice.stream().map(productMapper::getEntity).toList());
    }

    public ProductWithIdDto createProduct(ProductDto productToCreate) {
        try {
            return productMapper.postEntity(productToCreate);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public ProductWithIdDto updateProduct(ProductWithIdDto productToUpdate) {
        try {
            return productMapper.putEntity(productToUpdate);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void deleteProduct(UUID id) {
        try {
            List<Interest> interests = interestRepository.findByProduct(productRepository.findById(id).get());
            for(Interest interest:interests)
                interestRepository.delete(interest);
            productMapper.deleteEntity(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
