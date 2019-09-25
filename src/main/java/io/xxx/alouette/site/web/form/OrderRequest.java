package io.xxx.alouette.site.web.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class OrderRequest {

    private Long id;
    @NotEmpty(message = "订单明细未空")
    private List<ItemRequest> items;

    public int getGiftNum() {
        return items.stream()
                .filter(ItemRequest::isGiven)
                .map(ItemRequest::getSkuNum)
                .reduce(0, Integer::sum);
    }

    @Data
    public static class ItemRequest {

        @NotEmpty(message = "商品SKU编号为空")
        private String skuId;
        @NotEmpty(message = "商品SKU数量为空")
        private Integer skuNum;
        private boolean given;
        @NotEmpty(message = "明细行序号为空")
        private Integer serial;
    }
}
