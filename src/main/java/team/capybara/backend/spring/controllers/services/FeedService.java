package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.controllers.pagination.PaginationHandler;
import team.capybara.backend.spring.controllers.dto.entities.category.CategoryWithIdDto;
import team.capybara.backend.spring.controllers.dto.entities.product.ProductDto;
import team.capybara.backend.spring.controllers.dto.entities.product.ProductWithIdDto;
import team.capybara.backend.spring.controllers.dto.other.filters.FeedFilterEntity;
import team.capybara.backend.spring.controllers.mappers.entitymappers.ProductMapper;
import team.capybara.backend.spring.controllers.repositories.InterestRepository;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;
import team.capybara.backend.spring.entities.*;

import static team.capybara.backend.spring.Constants.EPSILON;

import java.util.*;

@Service
public final class FeedService {
    private final CategoryService categoryService;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final InterestRepository interestRepository;
    private final PaginationHandler<Product> paginationHandler;
    private final EntityHandler entityHandler;

    public FeedService(
            CategoryService categoryService,
            ProductMapper productMapper,
            ProductRepository productRepository,
            ProductTypeRepository productTypeRepository,
            InterestRepository interestRepository,
            PaginationHandler<Product> paginationHandler,
            EntityHandler entityHandler
    ) {
        this.categoryService = categoryService;
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
        this.interestRepository = interestRepository;
        this.paginationHandler = paginationHandler;
        this.entityHandler = entityHandler;
    }

    private List<Product> getAllProducts(
            FeedFilterEntity filter
    ) {
        filter.makeAllEmptyFieldsEquivalentToNull(
                filter.getLimit(),
                filter.getName(),
                filter.getIsOnlyFreeProducts(),
                filter.getShopsId(),
                filter.getCategoriesId(),
                filter.getDistance(),
                filter.getUserLat(),
                filter.getUserLon());

        List<Product> productsToSort;

        if (filter.getName() != null) {
            productsToSort = sortProductsByName(filter.getName());
        } else {
            productsToSort = productRepository.findAll();
        }

        if (filter.getIsOnlyFreeProducts() != null && filter.getIsOnlyFreeProducts()) {
            productsToSort = getAllFreeProducts(productsToSort);
        }

        if (filter.getCategoriesId() != null) {
            productsToSort = sortProductsByCategories(productsToSort, filter.getCategoriesId().stream().map(UUID::fromString).toList());
        }

        if (filter.getShopsId() != null) {
            productsToSort = sortProductsByShops(productsToSort, filter.getShopsId().stream().map(UUID::fromString).toList());
        }

        if (filter.getUserLat() != null && filter.getUserLon() != null) {
            productsToSort.forEach(product -> product.getProductType().getShop().calculateDistance(filter.getUserLat(), filter.getUserLon()));

            if (filter.getDistance() != null) {
                productsToSort = filterProductsByDistance(productsToSort, filter.getDistance());
            }

            productsToSort = recommendProductsByScore(productsToSort, filter.getUserLat(), filter.getUserLon());
        }

        return productsToSort;
    }

    public Integer getNumberOfPages(FeedFilterEntity filter) {
        List<Product> productsSorted = getAllProducts(filter);

        return paginationHandler.getNumberOfPages(productsSorted, filter.getLimit());
    }

    public Page<ProductWithIdDto> getProducts(int offset, FeedFilterEntity filter) {
        List<Product> productsSorted = getAllProducts(filter);
        List<Product> slice = paginationHandler.makeSliceFromList(productsSorted, offset, filter.getLimit());

        return new PageImpl<>(slice.stream().map(productMapper::getEntity).toList());

    }

    private List<Product> sortProductsByName(String name) {
        List<ProductType> productTypes = productTypeRepository.findByNameContainingIgnoreCase(name);
        List<Product> products = new ArrayList<>();

        for (ProductType productType : productTypes) {
            products.addAll(productRepository.findByProductType(productType));
        }

        return products;
    }

    private List<Product> getAllFreeProducts(List<Product> productsToSort) {
        List<Product> freeProducts = new ArrayList<>();

        for (Product product : productsToSort) {
            if ((double) product.getPrice() - (double) (product.getPrice() * product.getDiscount()) / 100 == 0.0) {
                freeProducts.add(product);
            }
        }

        return freeProducts;
    }

    private List<Product> sortProductsByCategories(List<Product> productsToSort, List<UUID> categoriesId) {
        List<UUID> requiredProductTypesId = new ArrayList<>();
        List<Product> productsSorted = new ArrayList<>();

        for (UUID categoryId : categoriesId) {
            Optional<CategoryWithIdDto> categoryDtoOptional = categoryService.getCategoryById(categoryId);

            categoryDtoOptional.ifPresent(categoryDto ->
                    requiredProductTypesId.addAll(categoryDto.productTypesId()));
        }

        for (Product product : productsToSort) {
            if (requiredProductTypesId.contains(product.getProductType().getId())) {
                productsSorted.add(product);
            }
        }

        return productsSorted;
    }

    private List<Product> sortProductsByShops(List<Product> productsToSort, List<UUID> shopsId) {
        List<Product> productsSorted = new ArrayList<>();

        for (Product product : productsToSort) {
            if (shopsId.contains(product.getProductType().getShop().getId())) {
                productsSorted.add(product);
            }
        }

        return productsSorted;
    }

    private List<Product> filterProductsByDistance(List<Product> productsToSort, Double distance) {
        List<Product> productsSorted = new ArrayList<>();

        for (Product product : productsToSort) {
            if ((product.getProductType().getShop().getDistance() - distance) < EPSILON) {
                productsSorted.add(product);
            }
        }

        return productsSorted;
    }

    private List<Product> recommendProductsByScore(List<Product> productsToSort, Double userLat, Double userLon) {
        if (productsToSort.size() <= 1) {
            return productsToSort;
        }

        Comparator<Product> comparator = Comparator.comparing(obj -> entityHandler.calculateProductScore(obj, userLat, userLon));
        productsToSort.sort(comparator);

        return productsToSort;
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
