package io.xxx.alouette.data;

import io.xxx.alouette.entity.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, Long> {
}
