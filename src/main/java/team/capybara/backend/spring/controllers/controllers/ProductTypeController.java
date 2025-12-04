package team.capybara.backend.spring.controllers.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.entities.category.CategoryWithIdDto;
import team.capybara.backend.spring.controllers.dto.entities.producttype.ProductTypeDto;
import team.capybara.backend.spring.controllers.dto.entities.producttype.ProductTypeWithIdDto;
import team.capybara.backend.spring.controllers.services.CategoryService;
import team.capybara.backend.spring.controllers.services.ProductTypeService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/product_types")
@SuppressWarnings(value = {"unused"})
public final class ProductTypeController {
    private static final Logger log = LoggerFactory.getLogger(ProductTypeController.class);

    private final CategoryService categoryService;
    private final ProductTypeService productTypeService;

    public ProductTypeController(
            CategoryService categoryService,
            ProductTypeService productTypeService
    ){
        this.categoryService = categoryService;
        this.productTypeService = productTypeService;
    }

    @GetMapping
    public ResponseEntity<List<ProductTypeWithIdDto>> getAllProductTypes() {
        log.info("Called getAllProductTypes");

        return ResponseEntity.ok(productTypeService.getAllProductTypes());
    }

    @GetMapping("/categories/{productTypeId}")
    public ResponseEntity<List<CategoryWithIdDto>> getAllCategoriesByProductTypeId(@PathVariable String productTypeId) {
        log.info("Called getAllCategoriesByProductTypeId; id={}", productTypeId);

        List<CategoryWithIdDto> categories = categoryService.getAllCategoriesByProductTypeId(UUID.fromString(productTypeId));

        return ResponseEntity.ok(categories);
    }

    /*
    //fix soon. Make optional instead of ServiceException
    @GetMapping("{shopId}")
    public ResponseEntity<List<ProductTypeDto>> getAllProductTypesByShopId(@PathVariable("shopId") String shopId) {
        log.info("Called getAllProductTypesByShopId; shopId={}", shopId);

        try {
            return ResponseEntity.ok(productTypeService.getAllProductTypes());
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<ProductTypeWithIdDto> getProductTypeById(@PathVariable("id") String id) {
        log.info("Called getProductTypeById; id={}", id);
        Optional<ProductTypeWithIdDto> productTypeDtoOptional = productTypeService.getProductTypeById(UUID.fromString(id));

        return productTypeDtoOptional.map(ResponseEntity::ok).orElseGet
                (() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/get_all_by_shop/{id}")
    public ResponseEntity<List<ProductTypeWithIdDto>> getProductTypeByShop(@PathVariable("id") String id) {
        log.info("Called getProductTypeById; id={}", id);
        Optional<List<ProductTypeWithIdDto>> allProductTypesByShopId = productTypeService.getAllProductTypesByShopId(UUID.fromString(id));

        return allProductTypesByShopId.map(ResponseEntity::ok).orElseGet
                (() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<ProductTypeWithIdDto> createProductType(@RequestBody ProductTypeDto productToCreate) {
        log.info("Called createProductType; productTypeToCreate={}", productToCreate);

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(productTypeService.createProductType(productToCreate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ProductTypeWithIdDto> updateProductType(@RequestBody ProductTypeWithIdDto productTypeToUpdate) {
        log.info("Called updateProductType; productTypeToUpdate={}", productTypeToUpdate);

        try {
            return ResponseEntity.ok(productTypeService.updateProductType(productTypeToUpdate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteProductType(@RequestBody String id) {
        log.info("Called deleteProductType; id={}", id);

        try {
            productTypeService.deleteProductType(UUID.fromString(id));
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
