package io.xxx.alouette.data.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String skuId) {
        super(String.format("商品[%s]不存在", skuId));
    }
}
