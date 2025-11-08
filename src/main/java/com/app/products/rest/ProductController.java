package com.app.products.rest;

import com.app.products.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody CreateProductRestModel product) {

        String productId;

        try {
            productId = productService.createProduct(product);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorMessage(new Date(), e.getMessage(), "/products")
            );
        }

        String safeOutput = HtmlUtils.htmlEscape(productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(safeOutput);
    }

}
