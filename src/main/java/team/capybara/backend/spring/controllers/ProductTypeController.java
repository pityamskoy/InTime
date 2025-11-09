package team.capybara.backend.spring.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.producttype.ProductTypeDto;
import team.capybara.backend.spring.controllers.services.ProductTypeService;
import team.capybara.backend.spring.entities.ProductType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/product_types")
@CrossOrigin(value = {"http://localhost:3000"})
@SuppressWarnings(value = {"unused"})
public final class ProductTypeController {
    private static final Logger log = LoggerFactory.getLogger(ProductTypeController.class);

    private final ProductTypeService productTypeService;

    public ProductTypeController(ProductTypeService productTypeService){
        this.productTypeService = productTypeService;
    }

    @GetMapping
    public ResponseEntity<List<ProductTypeDto>> getAllProductTypes() {
        log.info("Called getAllProductTypes");

        return ResponseEntity.ok(productTypeService.getAllProductTypes());
    }

    //fix soon. Make optional instead of ServiceException
    @GetMapping("/{shopId}")
    public ResponseEntity<List<ProductTypeDto>> getAllProductTypesByShopId(@PathVariable("shopId") String shopId) {
        log.info("Called getAllProductTypesByShopId; shopId={}", shopId);

        try {
            return ResponseEntity.ok(productTypeService.getAllProductTypes());
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductTypeDto> getProductTypeById(@PathVariable("id") String id) {
        log.info("Called getProductTypeById; id={}", id);
        Optional<ProductTypeDto> productTypeDtoOptional = productTypeService.getProductTypeById(UUID.fromString(id));

        return productTypeDtoOptional.map(ResponseEntity::ok).orElseGet
                (() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<ProductTypeDto> createProductType(@RequestBody ProductTypeDto productToCreate) {
        log.info("Called createProductType; productTypeToCreate={}", productToCreate);

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(productTypeService.createProductType(productToCreate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ProductTypeDto> updateProductType(@RequestBody ProductTypeDto productTypeToUpdate) {
        log.info("Called updateProductType; productTypeToUpdate={}", productTypeToUpdate);

        try {
            return ResponseEntity.ok(productTypeService.updateProductType(productTypeToUpdate));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ProductType> deleteProductType(@RequestBody String id) {
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
