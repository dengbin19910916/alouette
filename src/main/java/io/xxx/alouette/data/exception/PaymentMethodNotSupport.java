package io.xxx.alouette.data.exception;

public class PaymentMethodNotSupport extends RuntimeException {

    public PaymentMethodNotSupport() {
        super("支付方式不支持");
    }
}
