package io.xxx.alouette.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity(name = "stock")
public class StockEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sku_id", referencedColumnName = "skuId")
    private ProductEntity product;

    private Integer amount;

    @Data
    @Entity(name = "stock_change_log")
    public static class ChangeLogEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "sku_id", referencedColumnName = "skuId")
        private ProductEntity product;

        private Integer changeAmount;

        private LocalDateTime createdTime;
    }
}
