package io.xxx.alouette.site.service;

import io.xxx.alouette.data.PaymentMethodRepository;
import io.xxx.alouette.entity.PaymentMethodEntity;
import io.xxx.alouette.site.web.form.PaymentMethodRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public Long create(PaymentMethodRequest paymentMethodRequest) {
        PaymentMethodEntity paymentMethodEntity = new PaymentMethodEntity();
        BeanUtils.copyProperties(paymentMethodRequest, paymentMethodEntity);
        return paymentMethodRepository.save(paymentMethodEntity).getId();
    }
}
