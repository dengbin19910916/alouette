package io.xxx.alouette.site.web.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotEmpty(message = "商品名称为空")
    private String name;
    @NotEmpty(message = "商品SKU编码为空")
    private String skuId;
    @NotNull(message = "商品吊牌价为空")
    private BigDecimal tagPrice;
}
