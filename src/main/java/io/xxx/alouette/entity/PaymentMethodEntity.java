package io.xxx.alouette.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "payment_method")
public class PaymentMethodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "paymentMethod")
    private List<TradeOrderEntity> tradeOrders;

    @OneToMany(mappedBy = "paymentMethod")
    private List<HistoryOrderEntity> historyOrders;
}
