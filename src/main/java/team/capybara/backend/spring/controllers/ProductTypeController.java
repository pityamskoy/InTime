package team.capybara.backend.spring.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.capybara.backend.spring.controllers.services.ProductTypeService;
import team.capybara.backend.spring.entities.ProductType;

import java.util.List;
import java.util.Optional;

//fix urls/
@RestController
@CrossOrigin(value = {"http://localhost:3000"})
@RequestMapping("/patterns")
public final class ProductTypeController {
    private static final Logger log = LoggerFactory.getLogger(ShopController.class);

    private final ProductTypeService productTypeService;

    public ProductTypeController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @GetMapping
    public ResponseEntity<List<ProductType>> getAllProductTypes() {
        log.info("Called getAllProductTypes");

        return ResponseEntity.status(HttpStatus.OK).body(productTypeService.getAllProductTypes());
    }

    @GetMapping("/{shop_id}")
    public ResponseEntity<List<ProductType>> getAllProductTypesByShopId(@PathVariable("shop_id") String shopId) {
        log.info("Called getAllProductTypesByShopId shopId= " + shopId);
        try {
            List<ProductType> shopProductTypes = productTypeService.getAllProductTypes();

            return ResponseEntity.status(HttpStatus.OK).body(shopProductTypes);
        } catch (SecurityException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductType> getProductTypeById(@PathVariable("id") String id) {
        log.info("Called getProductTypeById id= " + id);

        Optional<ProductType> productType = productTypeService.getProductTypeById(id);

        if (productType.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(productType.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ProductType> createProductType(@RequestBody ProductType productType) {
        log.info("Called createProductType productType= " + productType);
        ProductType createdProductType = productTypeService.createProductType(productType);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdProductType);
    }
}
