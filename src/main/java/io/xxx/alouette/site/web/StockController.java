package io.xxx.alouette.site.web;

import io.xxx.alouette.site.service.StockService;
import io.xxx.alouette.site.web.form.StockRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/create")
    public Long create(@RequestBody StockRequest stockRequest) {
        return stockService.create(stockRequest);
    }
}
