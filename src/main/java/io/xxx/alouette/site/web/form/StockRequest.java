package io.xxx.alouette.site.web.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class StockRequest {

    @NotEmpty(message = "商品SKU为空")
    private String skuId;
    @NotEmpty(message = "商品SKU数量为空")
    private Integer amount;
}
