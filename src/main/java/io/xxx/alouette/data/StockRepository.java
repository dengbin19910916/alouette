package io.xxx.alouette.data;

import io.xxx.alouette.entity.ProductEntity;
import io.xxx.alouette.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<StockEntity, Long> {

    Optional<StockEntity> findByProduct(ProductEntity product);

}
