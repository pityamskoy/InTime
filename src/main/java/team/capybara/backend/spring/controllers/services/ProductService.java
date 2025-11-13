package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.category.CategoryDto;
import team.capybara.backend.spring.controllers.filters.FeedFilterEntity;
import team.capybara.backend.spring.entities.Product;
import team.capybara.backend.spring.controllers.dto.product.ProductDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.ProductMapper;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public final class ProductService {
    private final CategoryService categoryService;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public ProductService(
            CategoryService categoryService,
            ProductMapper productMapper,
            ProductRepository productRepository
    ) {
        this.categoryService = categoryService;
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(productMapper::getEntity).toList();
    }

    public List<ProductDto> getAllSortedProducts(FeedFilterEntity filter) {
        List<Product> productsToSort;

        if (filter.isOnlyFreeProducts()) {
            productsToSort = getAllFreeProducts();
        } else {
            productsToSort = productRepository.findAll();
        }

        if (filter.getCategoriesId() != null && !filter.getCategoriesId().isEmpty()) {
            productsToSort = sortProductsByCategories(productsToSort, filter.getCategoriesId().stream().map(UUID::fromString).toList());
        }

        if (filter.getShopsId() != null && !filter.getShopsId().isEmpty()) {
            productsToSort = sortProductsByShops(productsToSort, filter.getShopsId().stream().map(UUID::fromString).toList());
        }

        //add filterByDistance
        return productsToSort.stream().map(productMapper::getEntity).toList();
    }

    private List<Product> getAllFreeProducts() {
        List<Product> products = productRepository.findAll();
        List<Product> freeProducts = new ArrayList<>();

        for (Product product : products) {
            if (product.getPrice() == 0.0) {
                freeProducts.add(product);
            }
        }

        return freeProducts;
    }

    private List<Product> sortProductsByCategories(List<Product> productsToSort, List<UUID> categoriesId) {
        List<UUID> requiresProductTypesId = new ArrayList<>();
        List<Product> productsSorted = new ArrayList<>();

        for (UUID categoryId : categoriesId) {
            Optional<CategoryDto> categoryDtoOptional = categoryService.getCategoryById(categoryId);

            //fix check later
            assert categoryDtoOptional.isPresent();
            CategoryDto categoryDto = categoryDtoOptional.get();

            requiresProductTypesId.addAll(categoryDto.productTypesId());
        }

        for (Product product : productsToSort) {
            if (requiresProductTypesId.contains(product.getProductType().getId())) {
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

    public Optional<ProductDto> getProductById(UUID id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            ProductDto productDto = productMapper.getEntity(productOptional.get());
            return Optional.of(productDto);
        }

        return Optional.empty();
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
