package io.xxx.alouette.site.service;

import io.xxx.alouette.data.ProductRepository;
import io.xxx.alouette.entity.ProductEntity;
import io.xxx.alouette.site.web.form.ProductRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void create(ProductRequest productRequest) {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(productRequest, productEntity);
        productRepository.save(productEntity);
    }
}
