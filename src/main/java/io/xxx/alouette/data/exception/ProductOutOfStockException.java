package io.xxx.alouette.data.exception;

public class ProductOutOfStockException extends RuntimeException {

    public ProductOutOfStockException(String skuId) {
        super(String.format("商品[%s]无库存", skuId));
    }
}
