package io.xxx.alouette.data;

import io.xxx.alouette.entity.HistoryOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryOrderItemRepository extends JpaRepository<HistoryOrderEntity.ItemEntity, Long> {
}
