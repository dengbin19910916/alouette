package io.xxx.alouette.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity(name = "product")
public class ProductEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 300)
    private String name;

    @Column(length = 100, unique = true)
    private String skuId;

    @Column(precision = 16, scale = 2, nullable = false)
    private BigDecimal tagPrice;

    @OneToMany(mappedBy = "product")
    private List<TradeOrderEntity.ItemEntity> tradeOrderItems;

    @OneToMany(mappedBy = "product")
    private List<HistoryOrderEntity.ItemEntity> historyOrderItems;

    @OneToMany(mappedBy = "product")
    private List<StockEntity> stocks;

    @OneToMany(mappedBy = "product")
    private List<StockEntity.ChangeLogEntity> stockChangeLogs;
}
