package io.xxx.alouette.site.service;

import io.xxx.alouette.data.ProductRepository;
import io.xxx.alouette.data.StockRepository;
import io.xxx.alouette.entity.ProductEntity;
import io.xxx.alouette.entity.StockEntity;
import io.xxx.alouette.site.web.form.StockRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StockService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    public StockService(ProductRepository productRepository,
                        StockRepository stockRepository) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
    }

    public Long create(StockRequest stockRequest) {
        Optional<ProductEntity> productEntity = productRepository.findBySkuId(stockRequest.getSkuId());
        if (productEntity.isEmpty()) {
            throw new RuntimeException(String.format("商品[%s]不存在", stockRequest.getSkuId()));
        }

        StockEntity stockEntity = new StockEntity();
        BeanUtils.copyProperties(stockRequest, stockEntity);
        stockEntity.setProduct(productEntity.get());

        return stockRepository.save(stockEntity).getId();
    }
}
