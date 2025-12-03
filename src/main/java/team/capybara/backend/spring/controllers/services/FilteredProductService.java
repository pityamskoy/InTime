package team.capybara.backend.spring.controllers.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.controllers.pagination.PaginationHandler;
import team.capybara.backend.spring.controllers.dto.entities.category.CategoryWithIdDto;
import team.capybara.backend.spring.controllers.dto.entities.product.ProductWithIdDto;
import team.capybara.backend.spring.controllers.dto.other.filters.FeedFilterEntity;
import team.capybara.backend.spring.controllers.mappers.entitymappers.ProductMapper;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;
import team.capybara.backend.spring.entities.*;

import static team.capybara.backend.spring.Constants.EPSILON;

import java.util.*;

@Service
public final class FilteredProductService {
    private final CategoryService categoryService;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final PaginationHandler<Product> paginationHandler;
    private final EntityHandler entityHandler;

    public FilteredProductService(
            CategoryService categoryService,
            ProductMapper productMapper,
            ProductRepository productRepository,
            ProductTypeRepository productTypeRepository,
            PaginationHandler<Product> paginationHandler,
            EntityHandler entityHandler
    ) {
        this.categoryService = categoryService;
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
        this.paginationHandler = paginationHandler;
        this.entityHandler = entityHandler;
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
                filter.getUserLat(),
                filter.getUserLon());

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

        if (filter.getUserLat() != null && filter.getUserLon() != null) {
            productsToSort.forEach(product -> product.getProductType().getShop().calculateDistance(filter.getUserLat(), filter.getUserLon()));

            if (filter.getDistance() != null) {
                productsToSort = filterProductsByDistance(productsToSort, filter.getDistance());
            }

            productsToSort = recommendProductsByScore(productsToSort, filter.getUserLat(), filter.getUserLon());
        }

        List<Product> slice = paginationHandler.makeSliceFromList(productsToSort, offset, limit);

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

        Double pivot = entityHandler.calculateProductScore(productsToSort.getFirst(), userLat, userLon);
        List<Product> left = new ArrayList<>();
        List<Product> right = new ArrayList<>();

        for (int i = 0; i < productsToSort.size(); i++) {
            if (entityHandler.calculateProductScore(productsToSort.get(i), userLat, userLon) <= pivot && i != 0) {
                left.add(productsToSort.get(i));
            } else if (entityHandler.calculateProductScore(productsToSort.get(i), userLat, userLon) > pivot) {
                right.add(productsToSort.get(i));
            }
        }

        left = recommendProductsByScore(left, userLat, userLon);
        right = recommendProductsByScore(right, userLat, userLon);
        List<Product> mergedList = new ArrayList<>(left);
        mergedList.add(productsToSort.getFirst());
        mergedList.addAll(right);

        return mergedList;
    }
}
