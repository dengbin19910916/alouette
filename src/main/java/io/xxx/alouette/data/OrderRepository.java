package io.xxx.alouette.data;

import io.xxx.alouette.entity.TradeOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<TradeOrderEntity, Long> {
}
