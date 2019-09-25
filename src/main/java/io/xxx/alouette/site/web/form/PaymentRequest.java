package io.xxx.alouette.site.web.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Data
public class PaymentRequest {

    @NotEmpty(message = "订单号为空")
    private Long orderId;
    @NotEmpty(message = "支付金额为空")
    private BigDecimal amount;
    @NotEmpty(message = "支付方式为空")
    private Long paymentMethodId;
}
