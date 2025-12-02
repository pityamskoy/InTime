package team.capybara.backend.spring.controllers.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.entities.category.CategoryWithIdDto;
import team.capybara.backend.spring.controllers.dto.entities.product.ProductWithIdDto;
import team.capybara.backend.spring.controllers.dto.entities.user.UserAuthWithIdDto;
import team.capybara.backend.spring.controllers.dto.other.filters.FeedFilterEntity;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.UserConverter;
import team.capybara.backend.spring.controllers.mappers.entitymappers.ProductMapper;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;
import team.capybara.backend.spring.entities.Favorite;
import team.capybara.backend.spring.entities.Product;
import team.capybara.backend.spring.entities.ProductType;
import team.capybara.backend.spring.entities.User;

import java.util.*;

@Service
public final class FilteredProductService {
    private final CategoryService categoryService;
    private final FavoriteService favoriteService;
    private final UserService userService;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final UserConverter userConverter;

    public FilteredProductService(
            CategoryService categoryService,
            FavoriteService favoriteService,
            UserService userService,
            ProductMapper productMapper,
            ProductRepository productRepository,
            ProductTypeRepository productTypeRepository,
            UserConverter userConverter
    ) {
        this.categoryService = categoryService;
        this.favoriteService = favoriteService;
        this.userService = userService;
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
        this.userConverter = userConverter;
    }

    public Page<ProductWithIdDto> getAllSortedProducts(
            int offset,
            FeedFilterEntity filter
    ) {
        filter.makeAllEmptyFieldsEquivalentToNull(
                filter.getLimit(),
                filter.getName(),
                filter.getIsOnlyFreeProducts(),
                filter.getShopsId(),
                filter.getCategoriesId(),
                filter.getDistance(),
                filter.getUserId());

        int limit = filter.getLimit();
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

        if (filter.getDistance() != null) {
            productsToSort = sortProductsByDistance(productsToSort, filter.getDistance());
        }

        if (filter.getUserId() != null) {
            productsToSort = recommendProducts(productsToSort, filter.getUserId());
        }

        List<Product> slice = new ArrayList<>();
        if (!productsToSort.isEmpty()) {
            try {
                slice = productsToSort.subList(limit * (offset - 1), limit * (offset));
            } catch (IndexOutOfBoundsException _) {
                if (limit * (offset - 1) == productsToSort.size()) {
                    slice.add(productsToSort.get(limit * (offset - 1)));
                } else if (limit * (offset - 1) < productsToSort.size()) {
                    slice = productsToSort.subList(limit * (offset - 1), productsToSort.size());
                }
            }
        }
        slice.forEach(product -> product.calculateScore(1, 5));

        return new PageImpl<>(slice.stream().map(productMapper::getEntity).toList());
    }

    private List<Product> sortProductsByName(String name) {
        List<ProductType> productTypes = productTypeRepository.findByNameContainingIgnoreCase(name);
        List<Product> products = new ArrayList<>();

        for (ProductType productType : productTypes) {
            products.addAll(productRepository.findByProductType(productType));
        }

        products.forEach(product -> product.calculateScore(1, 5));

        return products;
    }

    private List<Product> getAllFreeProducts(List<Product> productsToSort) {
        List<Product> freeProducts = new ArrayList<>();

        for (Product product : productsToSort) {
            if (product.getPrice() - product.getDiscount() <= 0) {
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

    //add quicksort exactly in this method
    private List<Product> sortProductsByDistance(List<Product> productsToSort, Double distance) {
        List<Product> productsSorted = new ArrayList<>();

        for (Product product : productsToSort) {
            if (product.getProductType().getShop().getDistance() <= distance) {
                productsSorted.add(product);
            }
        }

        return productsSorted;
    }

    // It's necessary to add score to recommend by multiple parameters.
    // Also add quicksort algorithm for all criteria
    private List<Product> recommendProducts(
            List<Product> productsToSort,
            String userId
    ) {
        if (userId == null) {
            return productsToSort;
        }

        Optional<UserAuthWithIdDto> userDtoOptional = userService.getUserById(UUID.fromString(userId));

        if (userDtoOptional.isEmpty()) {
            return productsToSort;
        }

        User user = userConverter.toEntity(UUID.fromString(userId));
        List<Favorite> favoritesOfUser = favoriteService.getFavoritesByUser(user);

        List<Double> distances = new ArrayList<>();
        List<ProductType> productTypes = new ArrayList<>();
        for (Favorite favorite : favoritesOfUser) {
            productTypes.add(favorite.getProductType());
            distances.add(favorite.getProductType().getShop().getDistance());
        }

        //how to compare dates?
        List<Date> dates = new ArrayList<>();
        for (Product product : productsToSort) {
            if (productTypes.contains(product.getProductType())) {
                dates.add(product.getShelfLife());
            }
        }

        Double sumOfDistances = 0.0;
        for (Double distance : distances) {
            sumOfDistances += distance;
        }

        Double mediumDistance = sumOfDistances / distances.size();

        // add quicksort here
        List<Product> productsSorted = sortProductsByDistance(productsToSort, mediumDistance);
        for (Product product : productsToSort) {
            if (!productsSorted.contains(product)) {
                productsSorted.add(product);
            }
        }

        return productsSorted;
    }
}
