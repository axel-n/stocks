package com.bcs.stocks.controllers;

import com.bcs.stocks.models.StockRequest;
import com.bcs.stocks.models.StockResponse;
import com.bcs.stocks.services.internal.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    private Mono<StockResponse> getStatisticByParams(@RequestBody StockRequest request,
                                                     ServerHttpResponse response) {
        return stockService.getStatisticByParams(request)
                .switchIfEmpty(Mono.defer(() -> {
                    response.setStatusCode(HttpStatus.NOT_FOUND);
                    return Mono.empty();
                }));
    }
}
