package com.bcs.stocks.controllers;

import com.bcs.stocks.models.StockRequest;
import com.bcs.stocks.models.StockResponse;
import com.bcs.stocks.stockService.StockService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class StocksController {

    private final StockService stockService;

    public StocksController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping(value = "/stock/statistic")
    private Mono<StockResponse> getStatisticByParams(@RequestBody StockRequest request) {
        return Mono.empty();
    }
}
