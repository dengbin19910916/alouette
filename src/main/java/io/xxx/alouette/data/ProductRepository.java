package io.xxx.alouette.data;

import io.xxx.alouette.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findBySkuId(String skuId);
}
