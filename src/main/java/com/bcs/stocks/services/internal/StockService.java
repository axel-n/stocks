package com.bcs.stocks.services.internal;

import com.bcs.stocks.models.StockRequest;
import com.bcs.stocks.models.StockResponse;
import reactor.core.publisher.Mono;

public interface StockService {
    Mono<StockResponse> getStatisticByParams(StockRequest request);
}
