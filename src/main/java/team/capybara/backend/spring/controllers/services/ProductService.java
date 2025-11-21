package team.capybara.backend.spring.controllers.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.dto.category.CategoryDto;
import team.capybara.backend.spring.controllers.filters.FeedFilterEntity;
import team.capybara.backend.spring.controllers.repositories.ProductTypeRepository;
import team.capybara.backend.spring.entities.Product;
import team.capybara.backend.spring.controllers.dto.product.ProductDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.ProductMapper;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;
import team.capybara.backend.spring.entities.ProductType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public final class ProductService {
    private final CategoryService categoryService;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;

    public ProductService(
            CategoryService categoryService,
            ProductMapper productMapper,
            ProductRepository productRepository, ProductTypeRepository productTypeRepository
    ) {
        this.categoryService = categoryService;
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
    }

    public Page<ProductDto> getAllProducts(int offset, int limit) {
        Page<Product> products = productRepository.findAll(PageRequest.of(offset, limit));
        products.stream().forEach(product -> product.calculateScore(1,5));
        return products.map(productMapper::getEntity);
    }

    public Page<ProductDto> getAllSortedProducts(
            int offset,
            int limit,
            FeedFilterEntity filter
    ) {
        Page<Product> productsToSort;

        if (filter.isOnlyFreeProducts()) {
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
        productsToSort.stream().forEach(product -> product.calculateScore(1,5));
        //add filterByDistance
        return productsToSort.map(productMapper::getEntity);
    }

    private List<Product> getAllFreeProducts(int offset, int limit) {
        List<Product> freeProducts = new ArrayList<>();
        Page<Product> products = productRepository.findAll(PageRequest.of(offset, limit));

        while (freeProducts.size() < limit) {
            if (products.isEmpty()) {
                break;
            }

            for (Product product : products) {
                if (product.getPrice() == 0.0) {
                    freeProducts.add(product);
                }
            }

            products = productRepository.findAll(PageRequest.of(offset + 1, limit));
        }

        return freeProducts;
    }

    private List<Product> sortProductsByCategories(Page<Product> productsToSort, List<UUID> categoriesId) {
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

    private List<Product> sortProductsByShops(Page<Product> productsToSort, List<UUID> shopsId) {
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

    public List<ProductDto> getProductByName(String name) {
        List<ProductType>productTypes = productTypeRepository.findByNameContaining(name);
        List<Product>products = new ArrayList<>();
        for(ProductType productType : productTypes){
            products.addAll(productRepository.findByProductType(productType));
        }
        products.forEach(product -> product.calculateScore(1,5));
        return products.stream().map(productMapper::getEntity).toList();
    }

    public Page<ProductDto> getProductByShop(int offset, int limit, String id) {

        List<ProductType>productTypes = productTypeRepository.findAll();
        List<ProductType>productTypesWithNeededShop = new ArrayList<>();
        for(ProductType productType : productTypes){
            if(productType.getShop().getId().equals(UUID.fromString(id)))
                productTypesWithNeededShop.add(productType);
        }
        Page<Product> products = productRepository.findByProductTypeIn(productTypesWithNeededShop,PageRequest.of(offset, limit));
        products.stream().forEach(product -> product.calculateScore(1,5));
        return products.map(productMapper::getEntity);
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
