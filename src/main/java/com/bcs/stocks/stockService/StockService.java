package com.bcs.stocks.stockService;

import com.bcs.stocks.models.StockRequest;
import com.bcs.stocks.models.StockResponse;
import com.bcs.stocks.models.dict.Sector;
import reactor.core.publisher.Mono;

public interface StockService {
    Mono<StockResponse> getStatisticByParams(StockRequest request);
}
