package io.xxx.alouette.domain;

import io.xxx.alouette.data.ProductRepository;
import io.xxx.alouette.data.exception.ProductNotFoundException;
import io.xxx.alouette.entity.ProductEntity;
import io.xxx.alouette.site.web.form.OrderRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class PriceCalculator {

    private final ProductRepository productRepository;

    public PriceCalculator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public BigDecimal calculateOrderPrice(OrderRequest orderRequest) {
        return orderRequest.getItems().stream()
                .filter(itemRequest -> !itemRequest.isGiven())
                .map(itemRequest -> {
                    Optional<ProductEntity> product = productRepository.findBySkuId(itemRequest.getSkuId());
                    if (product.isEmpty()) {
                        throw new ProductNotFoundException(itemRequest.getSkuId());
                    }
                    return product.get().getTagPrice().multiply(new BigDecimal(itemRequest.getSkuNum()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
