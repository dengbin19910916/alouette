package io.xxx.alouette.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "trade_order")
public class TradeOrderEntity extends OrderEntity {

    @OneToMany(mappedBy = "tradeOrder", cascade = CascadeType.ALL)
    private List<ItemEntity> items;

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Entity(name = "trade_order_item")
    public static class ItemEntity extends OrderEntity.ItemEntity {

        @ManyToOne(cascade = CascadeType.ALL)
        private TradeOrderEntity tradeOrder;

        @Override
        public String toString() {
            return "ItemEntity{" +
                    "id=" + id +
                    ", product=" + product +
                    ", tagPrice=" + tagPrice +
                    ", skuNum=" + skuNum +
                    ", serial=" + serial +
                    ", createdTime=" + createdTime +
                    ", updatedTime=" + updatedTime +
                    '}';
        }
    }
}
