package team.capybara.backend.spring.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.dto.producttype.ProductTypeDto;
import team.capybara.backend.spring.controllers.mapping.entitymappers.ProductTypeMapper;
import team.capybara.backend.spring.controllers.services.ProductTypeService;
import team.capybara.backend.spring.controllers.services.ServiceException;
import team.capybara.backend.spring.entities.ProductType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(value = {"http://localhost:3000"})
@RequestMapping("/product_types")
public final class ProductTypeController {
    private static final Logger log = LoggerFactory.getLogger(ProductTypeController.class);

    private final ProductTypeService productTypeService;
    private final ProductTypeMapper productTypeMapper;

    public ProductTypeController(
            ProductTypeService productTypeService,
            ProductTypeMapper productTypeMapper) {
        this.productTypeService = productTypeService;
        this.productTypeMapper = productTypeMapper;
    }

    @GetMapping
    public ResponseEntity<List<ProductTypeDto>> getAllProductTypes() {
        log.info("Called getAllProductTypes");

        return ResponseEntity.status(HttpStatus.OK).body(productTypeService.getAllProductTypes());
    }

    @GetMapping("/{shop_id}")
    public ResponseEntity<List<ProductTypeDto>> getAllProductTypesByShopId(@PathVariable("shop_id") String shopId) {
        log.info("Called getAllProductTypesByShopId shopId= " + shopId);
        try {
            List<ProductTypeDto> shopProductTypes = productTypeService.getAllProductTypes();

            return ResponseEntity.status(HttpStatus.OK).body(shopProductTypes);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductTypeDto> getProductTypeById(@PathVariable("id") String id) {
        log.info("Called getProductTypeById id= " + id);

        Optional<ProductType> productType = productTypeService.getProductTypeById(UUID.fromString(id));

        if (productType.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(productTypeMapper.getEntity(productType.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ProductType> createProductType(@RequestBody ProductTypeDto productTypeDto) {
        log.info("Called createProductType productType= " + productTypeDto);
        ProductType createdProductType = productTypeService.createProductType(productTypeDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdProductType);
    }

    @PutMapping("/update")
    public ResponseEntity<ProductType> updateProductType(@RequestBody ProductTypeDto productTypeDto) {
        log.info("Called updateProductType productType= " + productTypeDto);

        try {
            ProductType productTypeUpdated = productTypeService.updateProductType(productTypeDto);
            return ResponseEntity.status(HttpStatus.OK).body(productTypeUpdated);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ProductType> deleteProductType(String id) {
        log.info("Called deleteProductType productType= {}", id);

        try {
            productTypeService.deleteProductType(UUID.fromString(id));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
