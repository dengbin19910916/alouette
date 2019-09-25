package io.xxx.alouette.site.web;

import io.xxx.alouette.site.service.OrderService;
import io.xxx.alouette.site.web.form.OrderRequest;
import io.xxx.alouette.site.web.form.PaymentRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public Long create(@RequestBody OrderRequest orderRequest) {
        return orderService.create(orderRequest);
    }

    @PostMapping("/pay")
    public void pay(@RequestBody PaymentRequest paymentRequest) {
        orderService.pay(paymentRequest);
    }

    @PostMapping("/cancel")
    public void cancel(@RequestParam Long orderId) {
        orderService.cancel(orderId);
    }
}
