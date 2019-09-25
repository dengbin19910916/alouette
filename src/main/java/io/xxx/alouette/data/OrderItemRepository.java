package io.xxx.alouette.data;

import io.xxx.alouette.entity.BaseOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<BaseOrderEntity.ItemEntity, Long> {
}
