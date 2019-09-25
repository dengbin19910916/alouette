package io.xxx.alouette.data.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long orderId) {
        super(String.format("订单[%s]不存在", orderId));
    }
}
