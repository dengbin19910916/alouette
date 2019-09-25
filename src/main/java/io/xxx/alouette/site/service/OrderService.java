package io.xxx.alouette.site.service;

import io.xxx.alouette.data.*;
import io.xxx.alouette.entity.*;
import io.xxx.alouette.site.web.form.OrderRequest;
import io.xxx.alouette.site.web.form.PaymentRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final HistoryOrderRepository historyOrderRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final StockChangeLogRepository stockChangeLogRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    public OrderService(OrderRepository orderRepository,
                        HistoryOrderRepository historyOrderRepository,
                        ProductRepository productRepository,
                        StockRepository stockRepository,
                        StockChangeLogRepository stockChangeLogRepository,
                        PaymentMethodRepository paymentMethodRepository) {
        this.orderRepository = orderRepository;
        this.historyOrderRepository = historyOrderRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.stockChangeLogRepository = stockChangeLogRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Transactional
    public Long create(OrderRequest orderRequest) {
        LocalDateTime now = LocalDateTime.now();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setGiftNum(orderRequest.getGiftNum());
        orderEntity.setStatus(BaseOrderEntity.Status.UNPAID);
        orderEntity.setCreatedTime(now);
        orderEntity.setUpdatedTime(now);

        orderEntity.setItems(orderRequest.getItems().stream()
                .map(itemRequest -> {
                    OrderEntity.ItemEntity itemEntity = new OrderEntity.ItemEntity();
                    BeanUtils.copyProperties(itemRequest, itemEntity);

                    Optional<ProductEntity> productEntity = productRepository.findBySkuId(itemRequest.getSkuId());
                    if (productEntity.isEmpty()) {
                        throw new RuntimeException(String.format("商品[%s]不存在", itemRequest.getSkuId()));
                    }
                    itemEntity.setOrder(orderEntity);
                    itemEntity.setProduct(productEntity.get());
                    itemEntity.setTagPrice(productEntity.get().getTagPrice());
                    itemEntity.setCreatedTime(orderEntity.getCreatedTime());
                    itemEntity.setUpdatedTime(orderEntity.getUpdatedTime());

                    Optional<StockEntity> stockEntity = stockRepository.findByProduct(itemEntity.getProduct());
                    if (stockEntity.isEmpty() || stockEntity.get().getAmount() == 0) {
                        throw new RuntimeException(String.format("商品[%s]无库存", itemRequest.getSkuId()));
                    } else {
                        stockEntity.get().setAmount(stockEntity.get().getAmount() - itemEntity.getSkuNum());
                        stockRepository.save(stockEntity.get());

                        StockEntity.ChangeLogEntity changeLogEntity = new StockEntity.ChangeLogEntity();
                        changeLogEntity.setProduct(itemEntity.getProduct());
                        changeLogEntity.setChangeAmount(-itemEntity.getSkuNum());
                        changeLogEntity.setCreatedTime(now);
                        stockChangeLogRepository.save(changeLogEntity);
                    }

                    return itemEntity;
                })
                .collect(Collectors.toList()));
        orderEntity.setPayableAmount(orderEntity.getItems().stream()
                .map(itemEntity -> itemEntity.getTagPrice().multiply(new BigDecimal(itemEntity.getSkuNum())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return orderRepository.save(orderEntity).getId();
    }

    @Transactional
    public void pay(PaymentRequest paymentRequest) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(paymentRequest.getOrderId());
        if (orderEntity.isEmpty()) {
            throw new RuntimeException(String.format("订单[%s]不存在", paymentRequest.getOrderId()));
        }

        orderEntity.get().setPaidAmount(paymentRequest.getAmount());

        Optional<PaymentMethodEntity> paymentMethodEntity = paymentMethodRepository
                .findById(paymentRequest.getPaymentMethodId());
        if (paymentMethodEntity.isEmpty()) {
            throw new RuntimeException("支付方式不存在");
        }
        orderEntity.get().setPaymentMethod(paymentMethodEntity.get());
        orderEntity.get().setPaidTime(LocalDateTime.now());
        orderEntity.get().setStatus(BaseOrderEntity.Status.PAID);
        finish(orderEntity.get());
    }

    @Transactional
    public void cancel(Long orderId) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        if (orderEntity.isEmpty()) {
            throw new RuntimeException(String.format("订单[%s]不存在", orderId));
        }

        orderEntity.get().setStatus(BaseOrderEntity.Status.CANCELED);
        finish(orderEntity.get());
    }

    private void finish(OrderEntity orderEntity) {
        HistoryOrderEntity historyOrderEntity = new HistoryOrderEntity();
        BeanUtils.copyProperties(orderEntity, historyOrderEntity);
        historyOrderEntity.setItems(orderEntity.getItems().stream()
                .map(itemEntity -> {
                    HistoryOrderEntity.ItemEntity historyItemEntity = new HistoryOrderEntity.ItemEntity();
                    BeanUtils.copyProperties(itemEntity, historyItemEntity);
                    historyItemEntity.setHistoryOrder(historyOrderEntity);
                    historyItemEntity.setProduct(itemEntity.getProduct());
                    return historyItemEntity;
                })
                .collect(Collectors.toList()));
        historyOrderRepository.save(historyOrderEntity);
        orderRepository.deleteById(orderEntity.getId());
    }
}
