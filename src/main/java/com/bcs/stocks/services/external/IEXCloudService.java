package com.bcs.stocks.services.external;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface IEXCloudService {
    Mono<Map<String, Object>> getDataByStocks(List<String> listSymbols);
}
