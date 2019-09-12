package com.bcs.stocks.controllers;

import com.bcs.stocks.models.StockRequest;
import com.bcs.stocks.models.StockResponse;
import com.bcs.stocks.services.internal.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class StocksController {

    private final StockService stockService;

    public StocksController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping(value = "/stocks/statistic")
    private Mono<StockResponse> getStatisticByParams(@RequestBody StockRequest request) {
        return stockService.getStatisticByParams(request);
    }
}
