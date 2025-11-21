package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.category.CategoryDto;
import team.capybara.backend.spring.controllers.dto.shop.ShopDto;
import team.capybara.backend.spring.controllers.dto.user.UserDto;
import team.capybara.backend.spring.controllers.filters.FeedFilterEntity;
import team.capybara.backend.spring.controllers.mappers.converters.entityconverters.UserConverter;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;
import team.capybara.backend.spring.entities.Favorite;
import team.capybara.backend.spring.entities.Product;
import team.capybara.backend.spring.controllers.dto.product.ProductDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.ProductMapper;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;
import team.capybara.backend.spring.entities.ProductType;
import team.capybara.backend.spring.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public final class ProductService {
    private final ShopService shopService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final FavoriteService favoriteService;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final UserConverter userConverter;

    public ProductService(
            ShopService shopService,
            CategoryService categoryService,
            UserService userService,
            FavoriteService favoriteService,
            ProductMapper productMapper,
            ProductRepository productRepository,
            ProductTypeRepository productTypeRepository,
            UserConverter userConverter
    ) {
        this.shopService = shopService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.favoriteService = favoriteService;
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
        this.userConverter = userConverter;
    }

    public Page<ProductDto> getAllProducts(int offset, int limit) {
        Page<Product> products = productRepository.findAll(PageRequest.of(offset, limit));
        products.stream().forEach(product -> product.calculateScore(1, 5));
        return products.map(productMapper::getEntity);
    }

    public Page<ProductDto> getAllSortedProducts(
            int offset,
            int limit,
            FeedFilterEntity filter
    ) {
        Page<Product> productsToSort;

        if (filter.getIsOnlyFreeProducts() != null && filter.getIsOnlyFreeProducts()) {
            productsToSort = new PageImpl<>(getAllFreeProducts(offset, limit));
        } else {
            productsToSort = productRepository.findAll(PageRequest.of(offset, limit));
        }

        if (filter.getCategoriesId() != null && !filter.getCategoriesId().isEmpty()) {
            productsToSort = new PageImpl<>(sortProductsByCategories(productsToSort, filter.getCategoriesId().stream().map(UUID::fromString).toList()));
        }

        if (filter.getShopsId() != null && !filter.getShopsId().isEmpty()) {
            productsToSort = new PageImpl<>(sortProductsByShops(productsToSort, filter.getShopsId().stream().map(UUID::fromString).toList()));
        }

        if (filter.getDistance() != null) {
            productsToSort = new PageImpl<>(sortProductsByDistance(productsToSort, filter.getShopsId(), filter.getDistance()));
        }

        if (filter.getUserId() != null && !filter.getUserId().isEmpty()) {
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
                if (product.getPrice() == 0.0) {
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
            shopsId = shopService.getAllShops().stream().map(ShopDto::id).toList();
        } else {
            shopsId = shopService.sortShopsByDistance(List.of(), distance).stream().map(ShopDto::id).toList();
        }

        for (Product product : productsToSort.getContent()) {
            if (shopsId.contains(product.getProductType().getShop().getId())) {
                productsSorted.add(product);
            }
        }

        return productsSorted;
    }

    private List<Product> recommendProducts(
            Page<Product> productsToSort,
            @Nullable List<String> shopsIdToSortProducts,
            UUID userId
    ) {
        Optional<UserDto> userDtoOptional = userService.getUserById(userId);

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

    public Optional<ProductDto> getProductById(UUID id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            ProductDto productDto = productMapper.getEntity(productOptional.get());
            return Optional.of(productDto);
        }

        return Optional.empty();
    }

    public List<ProductDto> getProductByName(String name) {
        List<ProductType>productTypes = productTypeRepository.findByNameContaining(name);
        List<Product>products = new ArrayList<>();
        for(ProductType productType : productTypes){
            products.addAll(productRepository.findByProductType(productType));
        }
        products.forEach(product -> product.calculateScore(1,5));
        return products.stream().map(productMapper::getEntity).toList();
    }

    public ProductDto createProduct(ProductDto productToCreate) {
        return productMapper.postEntity(productToCreate);
    }

    public ProductDto updateProduct(ProductDto productToUpdate) {
        try {
            return productMapper.putEntity(productToUpdate);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    public void deleteProduct(UUID id) {
        try {
            productMapper.deleteEntity(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
