package io.xxx.alouette.site.web;

import io.xxx.alouette.site.service.ProductService;
import io.xxx.alouette.site.web.form.ProductRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public void create(@RequestBody ProductRequest productRequest) {
        productService.create(productRequest);
    }
}
