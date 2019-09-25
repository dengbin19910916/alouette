package io.xxx.alouette.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class OrderEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected Integer giftNum;
    protected BigDecimal payableAmount;
    protected BigDecimal paidAmount;
    @ManyToOne
    protected PaymentMethodEntity paymentMethod;
    protected LocalDateTime createdTime;
    protected LocalDateTime updatedTime;
    protected LocalDateTime paidTime;
    @Enumerated
    protected Status status;

    @Data
    @MappedSuperclass
    public abstract static class ItemEntity implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        protected Long id;
        @ManyToOne
        @JoinColumn(name = "sku_id", referencedColumnName = "skuId")
        protected ProductEntity product;
        protected BigDecimal tagPrice;
        protected Integer skuNum;
        protected Integer serial;
        protected LocalDateTime createdTime;
        protected LocalDateTime updatedTime;

        @Override
        public String toString() {
            return "ItemEntity{" +
                    "product=" + product +
                    ", tagPrice=" + tagPrice +
                    ", skuNum=" + skuNum +
                    ", serial=" + serial +
                    ", createdTime=" + createdTime +
                    ", updatedTime=" + updatedTime +
                    '}';
        }
    }

    public enum Status {
        UNPAID,
        PAID,
        CANCELED
    }
}
