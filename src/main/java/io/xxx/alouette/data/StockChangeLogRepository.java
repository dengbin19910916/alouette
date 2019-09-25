package io.xxx.alouette.data;

import io.xxx.alouette.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockChangeLogRepository extends JpaRepository<StockEntity.ChangeLogEntity, Long> {
}
