package team.capybara.backend.spring.controllers.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.category.CategoryDto;
import team.capybara.backend.spring.controllers.dto.product.ProductWithIdDto;
import team.capybara.backend.spring.controllers.dto.shop.ShopWithIdDto;
import team.capybara.backend.spring.controllers.dto.user.UserAuthWithIdDto;
import team.capybara.backend.spring.controllers.filters.FeedFilterEntity;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.UserConverter;
import team.capybara.backend.spring.controllers.mappers.entitymappers.ProductMapper;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;
import team.capybara.backend.spring.entities.Favorite;
import team.capybara.backend.spring.entities.Product;
import team.capybara.backend.spring.entities.ProductType;
import team.capybara.backend.spring.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public final class FilteredProductService {
    private final ShopService shopService;
    private final CategoryService categoryService;
    private final FavoriteService favoriteService;
    private final UserService userService;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final UserConverter userConverter;

    public FilteredProductService(
            ShopService shopService,
            CategoryService categoryService,
            FavoriteService favoriteService,
            UserService userService,
            ProductMapper productMapper,
            ProductRepository productRepository,
            ProductTypeRepository productTypeRepository,
            UserConverter userConverter
    ) {
        this.shopService = shopService;
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
            int limit,
            FeedFilterEntity filter
    ) {
        Page<Product> productsToSort;

        if (filter.getIsOnlyFreeProducts() != null && filter.getIsOnlyFreeProducts()) {
            productsToSort = new PageImpl<>(getAllFreeProducts(offset, limit));
        } else {
            if (filter.getName() != null) {
                productsToSort = new PageImpl<>(sortProductsByName(filter.getName(), limit));
            } else {
                productsToSort = productRepository.findAll(PageRequest.of(offset, limit));
            }
        }

        if (filter.getCategoriesId() != null) {
            productsToSort = new PageImpl<>(sortProductsByCategories(productsToSort, filter.getCategoriesId().stream().map(UUID::fromString).toList()));
        }

        if (filter.getShopsId() != null) {
            productsToSort = new PageImpl<>(sortProductsByShops(productsToSort, filter.getShopsId().stream().map(UUID::fromString).toList()));
        }

        if (filter.getDistance() != null) {
            productsToSort = new PageImpl<>(sortProductsByDistance(productsToSort, filter.getShopsId(), filter.getDistance()));
        }

        if (filter.getUserId() != null) {
            productsToSort = new PageImpl<>(recommendProducts(productsToSort, filter.getShopsId(), UUID.fromString(filter.getUserId())));
        }

        productsToSort.stream().forEach(product -> product.calculateScore(1,5));

        return productsToSort.map(productMapper::getEntity);
    }

    private List<Product> getAllFreeProducts(int offset, int limit) {
        List<Product> freeProducts = new ArrayList<>();
        while (freeProducts.size() < limit) {
            Page<Product> productsOfPreviousPage = productRepository.findAll(PageRequest.of(offset, limit));
            Page<Product> productsOfNextPage = productRepository.findAll(PageRequest.of(offset + 1, limit));

            for (Product product : productsOfPreviousPage.getContent()) {
                if (product.getPrice() - product.getDiscount() <= 0) {
                    freeProducts.add(product);
                }
            }

            if (productsOfPreviousPage.getContent().size() < limit && productsOfNextPage.getContent().size() < limit) {
                break;
            }

            offset += 1;
        }

        return freeProducts;
    }

    private List<Product> sortProductsByCategories(Page<Product> productsToSort, List<UUID> categoriesId) {
        List<UUID> requiresProductTypesId = new ArrayList<>();
        List<Product> productsSorted = new ArrayList<>();

        for (UUID categoryId : categoriesId) {
            Optional<CategoryDto> categoryDtoOptional = categoryService.getCategoryById(categoryId);

            categoryDtoOptional.ifPresent(categoryDto ->
                    requiresProductTypesId.addAll(categoryDto.productTypesId()));
        }

        for (Product product : productsToSort.getContent()) {
            if (requiresProductTypesId.contains(product.getProductType().getId())) {
                productsSorted.add(product);
            }
        }

        return productsSorted;
    }

    private List<Product> sortProductsByShops(Page<Product> productsToSort, List<UUID> shopsId) {
        List<Product> productsSorted = new ArrayList<>();

        for (Product product : productsToSort.getContent()) {
            if (shopsId.contains(product.getProductType().getShop().getId())) {
                productsSorted.add(product);
            }
        }

        return productsSorted;
    }

    private List<Product> sortProductsByDistance(
            Page<Product> productsToSort,
            @Nullable List<String> shopsIdToSortProducts,
            Double distance
    ) {
        List<UUID> shopsId;
        List<Product> productsSorted = new ArrayList<>();

        if (shopsIdToSortProducts == null || shopsIdToSortProducts.isEmpty()) {
            shopsId = shopService.getAllShops().stream().map(ShopWithIdDto::id).toList();
        } else {
            shopsId = shopService.sortShopsByDistance(List.of(), distance).stream().map(ShopWithIdDto::id).toList();
        }

        for (Product product : productsToSort.getContent()) {
            if (shopsId.contains(product.getProductType().getShop().getId())) {
                productsSorted.add(product);
            }
        }

        return productsSorted;
    }

    private List<Product> sortProductsByName(
            String name,
            int limit
    ) {
        List<ProductType> productTypes = productTypeRepository.findByNameContaining(name);
        List<Product> products = new ArrayList<>();

        for (ProductType productType : productTypes) {
            if (products.size() >= limit) break;
            products.addAll(productRepository.findByProductType(productType));
        }

        products.forEach(product -> product.calculateScore(1,5));

        return products;
    }

    private List<Product> recommendProducts(
            Page<Product> productsToSort,
            @Nullable List<String> shopsIdToSortProducts,
            UUID userId
    ) {
        Optional<UserAuthWithIdDto> userDtoOptional = userService.getUserById(userId);

        if (userDtoOptional.isEmpty()) {
            return productsToSort.getContent();
        }

        User user = userConverter.toEntity(userId);
        List<Favorite> favoritesOfUser = favoriteService.getFavoritesByUser(user);

        List<Double> distances = new ArrayList<>();
        for (Favorite favorite : favoritesOfUser) {
            distances.add(favorite.getProductType().getShop().getDistance());
        }

        Double sumOfDistances = 0.0;
        for (Double distance : distances) {
            sumOfDistances += distance;
        }

        Double mediumDistance = sumOfDistances / distances.size();

        List<Product> productsSorted = sortProductsByDistance(productsToSort, shopsIdToSortProducts, mediumDistance);
        for (Product product : productsToSort.getContent()) {
            if (!productsSorted.contains(product)) {
                productsSorted.add(product);
            }
        }

        return productsSorted;
    }
}
