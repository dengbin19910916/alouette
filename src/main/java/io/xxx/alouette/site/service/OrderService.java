package io.xxx.alouette.site.service;

import io.xxx.alouette.data.*;
import io.xxx.alouette.data.exception.OrderNotFoundException;
import io.xxx.alouette.data.exception.PaymentMethodNotSupport;
import io.xxx.alouette.data.exception.ProductNotFoundException;
import io.xxx.alouette.data.exception.ProductOutOfStockException;
import io.xxx.alouette.domain.PriceCalculator;
import io.xxx.alouette.entity.*;
import io.xxx.alouette.site.web.form.OrderRequest;
import io.xxx.alouette.site.web.form.PaymentRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final PriceCalculator priceCalculator;

    public OrderService(OrderRepository orderRepository,
                        HistoryOrderRepository historyOrderRepository,
                        ProductRepository productRepository,
                        StockRepository stockRepository,
                        StockChangeLogRepository stockChangeLogRepository,
                        PaymentMethodRepository paymentMethodRepository,
                        PriceCalculator priceCalculator) {
        this.orderRepository = orderRepository;
        this.historyOrderRepository = historyOrderRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.stockChangeLogRepository = stockChangeLogRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.priceCalculator = priceCalculator;
    }

    @Transactional
    public Long create(OrderRequest orderRequest) {
        LocalDateTime now = LocalDateTime.now();
        TradeOrderEntity tradeOrderEntity = createTradeOrder(orderRequest, now);

        tradeOrderEntity.setItems(orderRequest.getItems().stream()
                .map(itemRequest -> {
                    TradeOrderEntity.ItemEntity itemEntity = createOrderItem(tradeOrderEntity, itemRequest);
                    removeOrderItemStockAmount(now, itemRequest, itemEntity.getProduct());
                    return itemEntity;
                })
                .collect(Collectors.toList()));
        tradeOrderEntity.setPayableAmount(priceCalculator.calculateOrderPrice(orderRequest));

        return orderRepository.save(tradeOrderEntity).getId();
    }

    private TradeOrderEntity createTradeOrder(OrderRequest orderRequest, LocalDateTime now) {
        TradeOrderEntity tradeOrderEntity = new TradeOrderEntity();
        tradeOrderEntity.setGiftNum(orderRequest.getGiftNum());
        tradeOrderEntity.setStatus(OrderEntity.Status.UNPAID);
        tradeOrderEntity.setCreatedTime(now);
        tradeOrderEntity.setUpdatedTime(now);
        return tradeOrderEntity;
    }

    private TradeOrderEntity.ItemEntity createOrderItem(TradeOrderEntity tradeOrderEntity,
                                                        OrderRequest.ItemRequest itemRequest) {
        TradeOrderEntity.ItemEntity itemEntity = new TradeOrderEntity.ItemEntity();
        BeanUtils.copyProperties(itemRequest, itemEntity);
        Optional<ProductEntity> productEntity = productRepository.findBySkuId(itemRequest.getSkuId());
        if (productEntity.isEmpty()) {
            throw new ProductNotFoundException(itemRequest.getSkuId());
        }
        itemEntity.setTradeOrder(tradeOrderEntity);
        itemEntity.setProduct(productEntity.get());
        itemEntity.setTagPrice(productEntity.get().getTagPrice());
        itemEntity.setCreatedTime(tradeOrderEntity.getCreatedTime());
        itemEntity.setUpdatedTime(tradeOrderEntity.getUpdatedTime());
        return itemEntity;
    }

    private void removeOrderItemStockAmount(LocalDateTime now,
                                            OrderRequest.ItemRequest itemRequest,
                                            ProductEntity productEntity) {
        Optional<StockEntity> stockEntity = stockRepository.findByProduct(productEntity);
        if (stockEntity.isEmpty() || stockEntity.get().getAmount() == 0) {
            throw new ProductOutOfStockException(itemRequest.getSkuId());
        }
        stockEntity.get().setAmount(stockEntity.get().getAmount() - itemRequest.getSkuNum());
        stockRepository.save(stockEntity.get());

        StockEntity.ChangeLogEntity changeLogEntity = new StockEntity.ChangeLogEntity();
        changeLogEntity.setProduct(productEntity);
        changeLogEntity.setChangeAmount(-itemRequest.getSkuNum());
        changeLogEntity.setCreatedTime(now);
        stockChangeLogRepository.save(changeLogEntity);
    }

    @Transactional
    public void pay(PaymentRequest paymentRequest) {
        Optional<TradeOrderEntity> orderEntity = orderRepository.findById(paymentRequest.getOrderId());
        if (orderEntity.isEmpty()) {
            throw new OrderNotFoundException(paymentRequest.getOrderId());
        }

        orderEntity.get().setPaidAmount(paymentRequest.getAmount());

        Optional<PaymentMethodEntity> paymentMethodEntity = paymentMethodRepository
                .findById(paymentRequest.getPaymentMethodId());
        if (paymentMethodEntity.isEmpty()) {
            throw new PaymentMethodNotSupport();
        }
        orderEntity.get().setPaymentMethod(paymentMethodEntity.get());
        orderEntity.get().setPaidTime(LocalDateTime.now());
        orderEntity.get().setStatus(OrderEntity.Status.PAID);
        finish(orderEntity.get());
    }

    @Transactional
    public void cancel(Long orderId) {
        Optional<TradeOrderEntity> orderEntity = orderRepository.findById(orderId);
        if (orderEntity.isEmpty()) {
            throw new OrderNotFoundException(orderId);
        }

        orderEntity.get().setStatus(OrderEntity.Status.CANCELED);
        finish(orderEntity.get());
    }

    private void finish(TradeOrderEntity tradeOrderEntity) {
        HistoryOrderEntity historyOrderEntity = new HistoryOrderEntity();
        BeanUtils.copyProperties(tradeOrderEntity, historyOrderEntity);
        historyOrderEntity.setItems(tradeOrderEntity.getItems().stream()
                .map(itemEntity -> {
                    HistoryOrderEntity.ItemEntity historyItemEntity = new HistoryOrderEntity.ItemEntity();
                    BeanUtils.copyProperties(itemEntity, historyItemEntity);
                    historyItemEntity.setHistoryOrder(historyOrderEntity);
                    historyItemEntity.setProduct(itemEntity.getProduct());
                    return historyItemEntity;
                })
                .collect(Collectors.toList()));
        historyOrderRepository.save(historyOrderEntity);
        orderRepository.deleteById(tradeOrderEntity.getId());
    }
}
