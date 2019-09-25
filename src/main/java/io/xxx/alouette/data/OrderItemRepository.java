package io.xxx.alouette.data;

import io.xxx.alouette.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderEntity.ItemEntity, Long> {
}
