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
@Entity(name = "history_order")
public class HistoryOrderEntity extends BaseOrderEntity {

    @OneToMany(mappedBy = "historyOrder", cascade = CascadeType.ALL)
    private List<ItemEntity> items;

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Entity(name = "history_order_item")
    public static class ItemEntity extends BaseOrderEntity.ItemEntity {

        @ManyToOne(cascade = CascadeType.ALL)
        private HistoryOrderEntity historyOrder;
    }
}
