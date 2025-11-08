package team.capybara.backend.spring.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.producttype.ProductTypeDto;
import team.capybara.backend.spring.controllers.mappers.entitymappers.ProductTypeMapper;
import team.capybara.backend.spring.controllers.services.ProductTypeService;
import team.capybara.backend.spring.controllers.services.exceptions.ServiceException;
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
    private final ProductTypeMapper productTypeMapper;

    public ProductTypeController(
            ProductTypeService productTypeService,
            ProductTypeMapper productTypeMapper
    ) {
        this.productTypeService = productTypeService;
        this.productTypeMapper = productTypeMapper;
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
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductTypeDto> getProductTypeById(@PathVariable("id") String id) {
        log.info("Called getProductTypeById; id={}", id);
        Optional<ProductType> productType = productTypeService.getProductTypeById(UUID.fromString(id));

        return productType.map(type -> ResponseEntity.ok(productTypeMapper.getEntity(type)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<ProductTypeDto> createProductType(@RequestBody ProductTypeDto productToCreate) {
        log.info("Called createProductType; productTypeToCreate={}", productToCreate);

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(productTypeService.createProductType(productToCreate));
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ProductTypeDto> updateProductType(@RequestBody ProductTypeDto productTypeToUpdate) {
        log.info("Called updateProductType; productTypeToUpdate={}", productTypeToUpdate);

        try {
            return ResponseEntity.ok(productTypeService.updateProductType(productTypeToUpdate));
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ProductType> deleteProductType(@RequestBody String id) {
        log.info("Called deleteProductType; id={}", id);

        try {
            productTypeService.deleteProductType(UUID.fromString(id));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
