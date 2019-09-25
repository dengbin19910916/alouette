package io.xxx.alouette.site.web;

import io.xxx.alouette.site.service.PaymentMethodService;
import io.xxx.alouette.site.web.form.PaymentMethodRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment-method")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @PostMapping("/create")
    public Long create(@RequestBody PaymentMethodRequest paymentMethodRequest) {
        return paymentMethodService.create(paymentMethodRequest);
    }
}
